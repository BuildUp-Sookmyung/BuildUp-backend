package buildup.server.controller;
import buildup.buildupback.dto.CreateLocalAccountDto;
import buildup.buildupback.dto.CreateLocalAccountRequestDto;
import buildup.buildupback.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class UserController {
    private final UserService userService;

    @PostMapping("/join/local/request")
    public Long joinByLocalAccount(@RequestBody CreateLocalAccountRequestDto createLocalAccountRequestDto) {
        return userService.join(createLocalAccountRequestDto.toEntity());
    }

}
