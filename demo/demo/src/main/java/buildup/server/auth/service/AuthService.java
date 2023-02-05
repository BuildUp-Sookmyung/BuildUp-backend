package buildup.server.auth.service;

import buildup.server.auth.domain.AuthToken;
import buildup.server.auth.domain.AuthTokenProvider;
import buildup.server.auth.domain.CustomUserDetails;
import buildup.server.common.AppProperties;
import buildup.server.dto.LocalJoinRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.Date;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final AuthenticationManager authenticationManager;
    private final AuthTokenProvider tokenProvider;
    private final AppProperties appProperties;

    public AuthToken createAuth(LocalJoinRequest request) {

        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        request.getUsername(),
                        request.getPassword()
                )
        );

        SecurityContextHolder.getContext().setAuthentication(authentication);
        Date now = new Date();

        AuthToken accessToken = tokenProvider.createAuthToken(
                request.getUsername(),
                ((CustomUserDetails) authentication.getPrincipal()).getRole().getKey(),
                new Date(now.getTime() + appProperties.getAuth().getTokenExpiry())
        );
        return accessToken;
    }
}
