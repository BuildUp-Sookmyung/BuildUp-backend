package buildup.server.member.controller;

import buildup.server.member.dto.ProfilePageResponse;
import buildup.server.member.service.ProfileService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class ProfileController {

    private final ProfileService profileService;

    @GetMapping
    public ProfilePageResponse showProfilePage() {
        return profileService.showProfilePage();
    }
}
