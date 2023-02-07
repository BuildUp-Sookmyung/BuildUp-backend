package buildup.server.member.controller;

import buildup.server.auth.domain.AuthInfo;
import buildup.server.auth.dto.TokenDto;
import buildup.server.common.response.StringResponse;
import buildup.server.member.dto.LocalJoinRequest;
import buildup.server.member.dto.LoginRequest;
import buildup.server.member.dto.SocialLoginRequest;
import buildup.server.member.service.MemberService;
import buildup.server.member.service.PhoneService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/member")
public class MemberController {
    private final MemberService memberService;
    private final PhoneService phoneService;

    @PostMapping("/local")
    public TokenDto joinByLocalAccount(@Valid @RequestBody LocalJoinRequest localJoinRequest) {
        String phone = localJoinRequest.getPhone();
        AuthInfo info = memberService.join(localJoinRequest);
        phoneService.savePhone(phone, info);
        return new TokenDto(info.getAccessToken().getToken(), info.getMemberRefreshToken().getRefreshToken());
    }

    @PostMapping("/login")
    public TokenDto signInByLocalAccount(@Valid @RequestBody LoginRequest loginRequest) {
        AuthInfo info = memberService.signIn(loginRequest);
        return new TokenDto(info.getAccessToken().getToken(), info.getMemberRefreshToken().getRefreshToken());
    }

    @PostMapping("/social")
    public TokenDto signInBySocialAccount(@Valid @RequestBody SocialLoginRequest request) {
        Optional<String> phone = Optional.ofNullable(request.getPhone());
        AuthInfo info = memberService.signIn(request);
        if (phone.isPresent())
            phoneService.savePhone(phone.get(), info);
        return new TokenDto(info.getAccessToken().getToken(), info.getMemberRefreshToken().getRefreshToken());
    }

    @PostMapping("/reissue")
    public TokenDto reissueToken(@Valid @RequestBody TokenDto tokenDto) {
        AuthInfo info = memberService.reissueToken(tokenDto);
        return new TokenDto(info.getAccessToken().getToken(), info.getMemberRefreshToken().getRefreshToken());
    }

    @GetMapping("/test")
    public StringResponse test() {
        return new StringResponse(memberService.test());
    }

}
