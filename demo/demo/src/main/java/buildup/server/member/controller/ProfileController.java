package buildup.server.member.controller;

import buildup.server.common.response.StringResponse;
import buildup.server.member.dto.ProfileHomeResponse;
import buildup.server.member.dto.ProfilePageResponse;
import buildup.server.member.dto.ProfileSaveRequest;
import buildup.server.member.service.ProfileService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.parameters.P;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/profiles")
@RequiredArgsConstructor
public class ProfileController {

    private final ProfileService profileService;

    @GetMapping("/me")
    public ProfilePageResponse showProfilePage() {
        return profileService.showProfilePage();
    }

    @GetMapping("/home")
    public ProfileHomeResponse showProfileHome() {
        return profileService.showProfileHome();
    }

    @PutMapping()
    public StringResponse updateProfile(@RequestBody ProfileSaveRequest request) {
        profileService.updateProfile(request);
        return new StringResponse("프로필을 수정하였습니다.");
    }

    @PutMapping("/img")
    public StringResponse updateProfileImage(MultipartFile img) {
        profileService.updateProfileImage(img);
        return new StringResponse("프로필 이미지를 수정하였습니다.");
    }
}
