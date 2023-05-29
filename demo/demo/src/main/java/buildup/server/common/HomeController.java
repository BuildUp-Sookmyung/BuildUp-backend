package buildup.server.common;

import buildup.server.auth.exception.AuthErrorCode;
import buildup.server.auth.exception.AuthException;
import buildup.server.common.response.ErrorEntity;
import buildup.server.common.response.StringResponse;
import buildup.server.member.domain.Member;
import buildup.server.member.service.MemberService;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.core.env.Environment;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Arrays;
import java.util.List;

@RequiredArgsConstructor
@RestController
public class HomeController {

    private final Environment env;
    private final MemberService memberService;

    @GetMapping("/home/profile")
    public StringResponse profile() {
        List<String> profiles = Arrays.asList(env.getActiveProfiles());
        List<String> realProfiles = Arrays.asList("real1", "real2");
        String defaultProfile = profiles.isEmpty() ? "default" : profiles.get(0);
        return new StringResponse(profiles.stream()
                .filter(realProfiles::contains)
                .findAny()
                .orElse(defaultProfile));
    }

    @GetMapping("/health")
    public StringResponse healthCheck() {return new StringResponse("HealthCheck");}

    @GetMapping("/test")
    public StringResponse test() {
        return new StringResponse(memberService.test());
    }

    @GetMapping("/home/entrypoint")
    public ErrorEntity authEntryPoint(HttpServletResponse response) {
        AuthException authException = new AuthException(AuthErrorCode.UNAUTHORIZED);
        return new ErrorEntity(authException.getErrorCode().toString(), response.getHeader("error"));
    }
}
