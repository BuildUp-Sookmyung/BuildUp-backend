package buildup.server.member;

import buildup.server.auth.domain.AuthInfo;
import buildup.server.auth.domain.TokenDto;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class MemberController {
    private final MemberService memberService;
    private final PhoneService phoneService;

    @PostMapping("/join/local")
    public TokenDto joinByLocalAccount(@Valid @RequestBody LocalJoinRequest localJoinRequest) {
        String phone = localJoinRequest.getPhone();
        AuthInfo info = memberService.join(localJoinRequest);
        phoneService.savePhone(phone, info);
        return new TokenDto(info.getAccessToken().getToken(), info.getMemberRefreshToken().getRefreshToken());
    }

    @PostMapping("/login/local")
    public TokenDto signInByLocalAccount(@Valid @RequestBody LoginRequest loginRequest) {
        AuthInfo info = memberService.signIn(loginRequest);
        return new TokenDto(info.getAccessToken().getToken(), info.getMemberRefreshToken().getRefreshToken());
    }

    @PostMapping("/login/social")
    public TokenDto signInBySocialAccount(@Valid @RequestBody SocialLoginRequest request) {
        AuthInfo info = memberService.signIn(request);
        return new TokenDto(info.getAccessToken().getToken(), info.getMemberRefreshToken().getRefreshToken());
    }

    @PostMapping("/reissue")
    public TokenDto reissueToken(@Valid @RequestBody TokenDto tokenDto) {
        AuthInfo info = memberService.reissueToken(tokenDto);
        return new TokenDto(info.getAccessToken().getToken(), info.getMemberRefreshToken().getRefreshToken());
    }

    @GetMapping("/test")
    public String test() {
        return memberService.test();
    }

}
