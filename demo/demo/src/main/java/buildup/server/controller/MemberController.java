package buildup.server.controller;

import buildup.server.auth.domain.AuthInfo;
import buildup.server.auth.domain.TokenResponse;
import buildup.server.domain.member.Phone;
import buildup.server.domain.member.PhoneRepository;
import buildup.server.domain.member.PhoneService;
import buildup.server.dto.LocalJoinRequest;
import buildup.server.service.MemberService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.Optional;

@RestController
@RequiredArgsConstructor
public class MemberController {
    private final MemberService memberService;
    private final PhoneService phoneService;

    @PostMapping("/join/local")
    public TokenResponse joinByLocalAccount(@RequestBody LocalJoinRequest localJoinRequest) {
        AuthInfo info = memberService.join(localJoinRequest);
        phoneService.savePhone(info);
        return new TokenResponse(info.getAccessToken().getToken(), info.getMemberRefreshToken().getRefreshToken());
    }

}
