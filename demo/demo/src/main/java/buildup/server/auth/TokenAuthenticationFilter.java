package buildup.server.auth;

import buildup.server.auth.domain.AuthToken;
import buildup.server.auth.domain.AuthTokenProvider;
import buildup.server.common.HeaderUtil;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.List;

@Slf4j
@RequiredArgsConstructor
public class TokenAuthenticationFilter extends OncePerRequestFilter {

    private final AuthTokenProvider tokenProvider;
    private static final List<String> EXCLUDE_URL =
            List.of(
                    "/member/email",
                    "/member/code",
                    "/member/local",
                    "/member/social-access",
                    "/member/social-profile",
                    "/member/social-token",
                    "/member/login",
                    "/member/reissue",
                    "/member/find-id",
                    "/member/id-check",
                    "/member/find-pw",
                    "/member/time",
                    "/health",
                    "/home/entrypoint"
            );

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain) throws ServletException, IOException {
        String tokenStr = HeaderUtil.getAccessToken(request);
        AuthToken token = tokenProvider.convertAuthToken(tokenStr);
        log.info("TokenAuthenticationFilter works");

        if (token.validate()) {
            Authentication authentication = tokenProvider.getAuthentication(token);
            SecurityContextHolder.getContext().setAuthentication(authentication);
        }
        filterChain.doFilter(request, response);

    }

    @Override
    protected boolean shouldNotFilter(HttpServletRequest request) {
        return EXCLUDE_URL.stream().anyMatch(exclude -> exclude.equalsIgnoreCase(request.getServletPath()));
    }
}
