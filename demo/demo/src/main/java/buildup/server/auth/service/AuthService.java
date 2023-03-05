package buildup.server.auth.service;

import buildup.server.auth.domain.*;
import buildup.server.auth.dto.TokenDto;
import buildup.server.auth.dto.TokenRequestDto;
import buildup.server.auth.exception.AuthErrorCode;
import buildup.server.auth.exception.AuthException;
import buildup.server.auth.repository.RefreshTokenRepository;
import buildup.server.common.AppProperties;
import buildup.server.member.domain.Member;
import buildup.server.member.domain.Role;
import buildup.server.member.dto.LoginRequest;
import buildup.server.member.exception.MemberErrorCode;
import buildup.server.member.exception.MemberException;
import buildup.server.member.repository.MemberRepository;
import io.jsonwebtoken.Claims;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;

@Slf4j
@RequiredArgsConstructor
@Service
public class AuthService {

    private final MemberRepository memberRepository;
    private final AuthTokenProvider tokenProvider;
    private final RefreshTokenRepository refreshTokenRepository;
    private final AppProperties appProperties;
    private final AuthenticationManager authenticationManager;
    private static final long THREE_DAYS_MSEC = 259200000;

    //TODO: 리팩토링-중복코드 제거

    // 액세스 토큰 발급
    @Transactional
    public AuthToken createAuth(LoginRequest request) {

        try{
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
//                    ((UserAdapter)authentication.getPrincipal()).getMember().getRoleKey(),
                    ((CustomUserDetails)authentication.getPrincipal()).getRole().getKey(),
                    new Date(now.getTime() + appProperties.getAuth().getTokenExpiry())
            );

            return accessToken;
        } catch (BadCredentialsException e) {
            throw new AuthException(AuthErrorCode.CREDENTIAL_MISS_MATCH);
        }
    }

    // 리프레시 토큰 발급 및 저장 또는 수정
    @Transactional
    public MemberRefreshToken setRefreshToken(LoginRequest request) {
        Date now = new Date();
        String username = request.getUsername();

        long refreshTokenExpiry = appProperties.getAuth().getRefreshTokenExpiry();
        AuthToken refreshToken = tokenProvider.createAuthToken(
                appProperties.getAuth().getTokenSecret(),
                new Date(now.getTime() + refreshTokenExpiry)
        );

        //userId refresh token 으로 DB 확인
        MemberRefreshToken memberRefreshToken = refreshTokenRepository.findByUsername(username);
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

    // TODO: 예외처리
    @Transactional
    public AuthInfo reissueToken(TokenDto dto) {
        AuthToken expiredToken = tokenProvider.convertAuthToken(dto.getAccessToken());
        AuthToken refreshToken = tokenProvider.convertAuthToken(dto.getRefreshToken());

        Claims claims = expiredToken.getExpiredTokenClaims();
        if (claims == null) {
            throw new AuthException(AuthErrorCode.NOT_EXPIRED_TOKEN_YET);
        } else {
            log.info("claims={}", claims);
        }
        String username = claims.getSubject();
        Role role = Role.of(claims.get("role", String.class));

        if (!refreshToken.validate()) {
            throw new AuthException(AuthErrorCode.INVALID_REFRESH_TOKEN);
        }

        // refresh token으로 DB에서 user 정보와 확인
        MemberRefreshToken memberRefreshToken = refreshTokenRepository.findByUsernameAndRefreshToken(username, dto.getRefreshToken());
        log.info("UserRefreshToken={}", refreshToken);
        if (memberRefreshToken == null) {
            throw new AuthException(
                    AuthErrorCode.INVALID_REFRESH_TOKEN,
                    "가입되지 않은 회원이거나 유효하지 않은 리프레시 토큰입니다."
            );
        }

        Date now = new Date();

        AuthToken newAccessToken = tokenProvider.createAuthToken(
                username,
                role.getKey(),
                new Date(now.getTime() + appProperties.getAuth().getTokenExpiry())
        );

        long validTime = refreshToken.getTokenClaims().getExpiration().getTime() - now.getTime();

        // 리프레시 토큰 만료기간이 3일 이하인 경우 refresh token 발급
        if (validTime <= THREE_DAYS_MSEC) {
            long refreshTokenExpiry = appProperties.getAuth().getRefreshTokenExpiry();

            refreshToken = tokenProvider.createAuthToken(
                    appProperties.getAuth().getTokenSecret(),
                    new Date(now.getTime() + refreshTokenExpiry)
            );
            // DB에 토큰 업데이트
            memberRefreshToken.setRefreshToken(refreshToken.getToken());
        }

        return new AuthInfo(newAccessToken, memberRefreshToken);
    }


}
