package buildup.server.auth.service;

import buildup.server.auth.domain.*;
import buildup.server.auth.repository.RefreshTokenRepository;
import buildup.server.common.AppProperties;
import buildup.server.dto.LocalJoinRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final AuthenticationManager authenticationManager;
    private final AuthTokenProvider tokenProvider;
    private final AppProperties appProperties;
    private final RefreshTokenRepository refreshTokenRepository;


    public AuthToken createAuth(LocalJoinRequest request) {

        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        request.getUsername(),
                        request.getPassword()
                )
        );

        SecurityContextHolder.getContext().setAuthentication(authentication);
        Date now = new Date();
        String username = request.getUsername();

        AuthToken accessToken = tokenProvider.createAuthToken(
                username,
                ((CustomUserDetails) authentication.getPrincipal()).getRole().getKey(),
                new Date(now.getTime() + appProperties.getAuth().getTokenExpiry())
        );

        return accessToken;
    }

    public MemberRefreshToken setRefreshToken(LocalJoinRequest request) {

        Date now = new Date();
        String username = request.getUsername();

        long refreshTokenExpiry = appProperties.getAuth().getRefreshTokenExpiry();
        AuthToken refreshToken = tokenProvider.createAuthToken(
                appProperties.getAuth().getTokenSecret(),
                new Date(now.getTime() + refreshTokenExpiry)
        );

        //userId refresh token 으로 DB 확인
        MemberRefreshToken memberRefreshToken = refreshTokenRepository.findByUserId(username);
        if (memberRefreshToken == null) {
            // 없으면 새로 등록
            memberRefreshToken = new MemberRefreshToken(username, refreshToken.getToken());
            refreshTokenRepository.save(memberRefreshToken);
        } else {
            // DB에 refresh token 업데이트
            memberRefreshToken.setRefreshToken(refreshToken.getToken());
        }

        return memberRefreshToken;
    }
}
