package buildup.server.member.service;

import buildup.server.entity.Interest;
import buildup.server.member.domain.Member;
import buildup.server.member.domain.Profile;
import buildup.server.member.dto.ProfileHomeResponse;
import buildup.server.member.dto.ProfilePageResponse;
import buildup.server.member.dto.ProfileSaveRequest;
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

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class ProfileService {

    private final MemberRepository memberRepository;
    private final ProfileRepository profileRepository;
    private final InterestRepository interestRepository;
    private final S3Service s3Service;

    @Transactional
    public Long saveProfile(ProfileSaveRequest request, Member member, MultipartFile img) {
        Profile profile = request.toProfile();
        String url = null;
        if (! img.isEmpty())
            url = s3Service.uploadProfile(profile, member.getId(), img);
        saveInterests(request.getInterests(), profile);
        profile.setMember(member);
        profile.setImgUrl(url);
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

        // 관심분야 리스트 수정 - 기존 Interest 모두 삭제하고 다시 저장
        for (Interest interest : profile.getInterests()) {
            interestRepository.delete(interest);
        }
        profile.getInterests().clear();
        saveInterests(request.getInterests(), profile);
    }

    @Transactional
    public void updateProfileImage( MultipartFile img) {
        Member member = findCurrentMember();
        Profile profile = profileRepository.findById(member.getId()).get();

        String imgUrl = profile.getImgUrl();

        if (! img.isEmpty()) {
            // 일단 입력이 있으면 업로드. 기존 이미지 있어도 overwrite
            String url = s3Service.uploadProfile(profile, member.getId(), img);
            profile.setImgUrl(url);
        } else if (imgUrl!=null) {
            // 입력이 없는데 기존 이미지가 있었던 경우 -> 이미지 삭제
            s3Service.deleteProfile(imgUrl);
            profile.setImgUrl(null);
        }
    }

    private void saveInterests(List<String> requestList, Profile profile) {
        for (String interest : requestList) {
            Interest select = interestRepository.save(new Interest(profile, interest));
            profile.getInterests().add(select);
        }
    }

    // TODO: 로그인한 사용자
    private Member findCurrentMember() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        Member user = memberRepository.findByUsername(authentication.getName()).get();
        return user;
    }
}
