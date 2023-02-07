package buildup.server.common;

import buildup.server.auth.AuthErrorCode;
import buildup.server.auth.AuthException;
import buildup.server.common.response.ErrorEntity;
import buildup.server.common.response.StringResponse;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class HomeController {

    @GetMapping("/home/health")
    public StringResponse health() {
        return new StringResponse("Health Check");
    }

    @GetMapping("/home/entrypoint")
    public ErrorEntity authEntryPoint() {
        AuthException authException = new AuthException(AuthErrorCode.UNAUTHORIZED);
        return new ErrorEntity(authEntryPoint().getErrorCode(), "AuthenticationEntryPoint");
    }
}
