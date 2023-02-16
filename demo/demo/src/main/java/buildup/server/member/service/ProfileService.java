package buildup.server.member.service;

import buildup.server.member.domain.Member;
import buildup.server.member.domain.Profile;
import buildup.server.member.dto.InterestsSaveRequest;
import buildup.server.member.dto.ProfileSaveRequest;
import buildup.server.member.repository.ProfileRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@Slf4j
@Service
@RequiredArgsConstructor
public class ProfileService {

    private final ProfileRepository profileRepository;
    private final S3Service s3Service;

    public Long saveProfile(ProfileSaveRequest request, Member member, MultipartFile img) throws IOException {
        Profile profile = request.toProfile();
        String url = null;
        if (! img.isEmpty())
            url = s3Service.uploadProfile(profile, img);
        ProfileSaveRequest.saveInterests(request.getInterests(), profile);
        profile.setMember(member);
        profile.setImgUrl(url);
        return profileRepository.save(profile).getId();
    }
}
