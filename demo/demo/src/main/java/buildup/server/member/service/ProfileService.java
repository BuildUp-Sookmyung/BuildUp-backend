package buildup.server.member.service;

import buildup.server.activity.domain.Activity;
import buildup.server.activity.repository.ActivityRepository;
import buildup.server.auth.domain.MemberRefreshToken;
import buildup.server.auth.dto.TokenRequestDto;
import buildup.server.auth.exception.AuthErrorCode;
import buildup.server.auth.exception.AuthException;
import buildup.server.auth.repository.RefreshTokenRepository;
import buildup.server.entity.Interest;
import buildup.server.member.domain.Member;
import buildup.server.member.domain.Profile;
import buildup.server.member.dto.ProfileHomeResponse;
import buildup.server.member.dto.ProfilePageResponse;
import buildup.server.member.dto.ProfileSaveRequest;
import buildup.server.member.exception.MemberErrorCode;
import buildup.server.member.exception.MemberException;
import buildup.server.member.repository.InterestRepository;
import buildup.server.member.repository.MemberRepository;
import buildup.server.member.repository.ProfileRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Slf4j
@Service
@RequiredArgsConstructor
public class ProfileService {

    private final MemberRepository memberRepository;
    private final ProfileRepository profileRepository;
    private final InterestRepository interestRepository;
    private final ActivityRepository activityRepository;
    private final RefreshTokenRepository refreshTokenRepository;
    private final S3Service s3Service;

    @Transactional
    public Long saveProfile(ProfileSaveRequest request, Member member) {
        Profile profile = request.toProfile();
        String url = null;
        saveInterests(request.getInterests(), profile);
        profile.setMember(member);
        return profileRepository.save(profile).getId();
    }

    @Transactional(readOnly = true)
    public ProfilePageResponse showProfilePage() {
        Member member = findCurrentMember();
        return ProfilePageResponse.of(profileRepository.findById(member.getId()).get());
    }

    @Transactional(readOnly = true)
    public ProfileHomeResponse showProfileHome() {
        Member member = findCurrentMember();
        return ProfileHomeResponse.toDto(profileRepository.findById(member.getId()).get());
    }

    @Transactional
    public void updateProfile(ProfileSaveRequest request) {
        Member member = findCurrentMember();
        Profile profile = profileRepository.findById(member.getId()).get();
        request.updateProfile(profile);

        // ???????????? ????????? ?????? - ?????? Interest ?????? ???????????? ?????? ??????
        interestRepository.deleteAll(profile.getInterests());
        profile.getInterests().clear();
        saveInterests(request.getInterests(), profile);
    }

    @Transactional
    public void updateProfileImage(MultipartFile img) {
        Member member = findCurrentMember();
        Profile profile = profileRepository.findById(member.getId()).get();

        String imgUrl = profile.getImgUrl();

        if (img==null)
            throw new MemberException(MemberErrorCode.MEMBER_PROFILE_BAD_REQUEST);

        // ????????? ????????? ?????????
        if (! img.isEmpty()) {
            //?????? ????????? ????????? delete
            if (imgUrl != null)
                s3Service.deleteProfile(imgUrl);
            String url = s3Service.uploadProfile(member.getId(), img);
            profile.setImgUrl(url);
        } else { // ?????? ????????? ?????? ??? ??????
            if (imgUrl != null) {
                s3Service.deleteProfile(imgUrl);
                profile.setImgUrl(null);
            }
        }
    }

    @Transactional
    public List<ProfileHomeResponse> searchProfilesByKeyword(String keyword) {
        List<Profile> profilesByInterest = interestRepository.findAllByFieldContaining(keyword).stream()
                .map(Interest::getProfile).distinct().collect(Collectors.toList());
        List<Profile> profilesByActivity = findProfilesById(activityRepository.findAllByNameContaining(keyword).stream()
                .map(Activity::getMember).map(Member::getId).distinct().collect(Collectors.toList()));

        List<Profile> list = Stream.of(profilesByInterest, profilesByActivity)
                .flatMap(Collection::stream).distinct().toList();

        List<ProfileHomeResponse> result = new ArrayList<>();
        for (Profile profile : list)
            result.add(ProfileHomeResponse.toDto(profile));
        Collections.sort(result);
        return result;
    }

    private List<Profile> findProfilesById(List<Long> idList) {
        ArrayList<Profile> profiles = new ArrayList<>();
        for (Long id: idList) {
            Profile profile = profileRepository.findById(id)
                    .orElseThrow(() -> new MemberException(MemberErrorCode.MEMBER_NOT_FOUND));
            profiles.add(profile);
        }
        return profiles;
    }

    private void saveInterests(List<String> requestList, Profile profile) {
        for (String interest : requestList) {
            Interest select = interestRepository.save(new Interest(profile, interest));
            profile.getInterests().add(select);
        }
    }

    // TODO: ???????????? ?????????
    private Member findCurrentMember() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        Member user = memberRepository.findByUsername(authentication.getName())
                .orElseThrow(() -> new MemberException(MemberErrorCode.MEMBER_NOT_FOUND));
        return user;
    }

    @Transactional
    public void deleteRefreshToken(TokenRequestDto tokenRequestDto) {
        String memberId = findCurrentMember().getUsername();
        MemberRefreshToken memberRefreshToken = refreshTokenRepository.findByUsernameAndRefreshToken(memberId, tokenRequestDto.getRefreshToken());
        if (memberRefreshToken == null) {
            throw new AuthException(AuthErrorCode.INVALID_REFRESH_TOKEN, "???????????? ?????? ??????????????? ???????????? ?????? ???????????? ???????????????.");
        }
        refreshTokenRepository.deleteById(memberRefreshToken.getRefreshTokenId());
    }

}
