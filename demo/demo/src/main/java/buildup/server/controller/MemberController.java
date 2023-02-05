package buildup.server.controller;

import buildup.server.dto.LocalJoinRequest;
import buildup.server.service.MemberService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class MemberController {
    private final MemberService memberService;

    @PostMapping("/join/local/request")
    public Long joinByLocalAccount(@RequestBody LocalJoinRequest localJoinRequest) {
        return memberService.join(localJoinRequest.toEntity());
    }

}
