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

    @PostMapping("/join/local")
    public Long joinByLocalAccount(@RequestBody LocalJoinRequest localJoinRequest) {
        Long new_id = memberService.join(localJoinRequest);
        if (localJoinRequest.getPhone() != null) {
            //TODO: 전화번호 저장하는 로직
        }
        return new_id;
    }

}
