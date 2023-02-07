package buildup.server.member.service;

import buildup.server.auth.AuthErrorCode;
import buildup.server.auth.AuthException;
import buildup.server.auth.domain.*;
import buildup.server.auth.repository.RefreshTokenRepository;
import buildup.server.common.AppProperties;
import buildup.server.member.domain.Member;
import buildup.server.member.domain.Role;
import buildup.server.member.dto.LocalJoinRequest;
import buildup.server.member.dto.LoginRequest;
import buildup.server.member.dto.SocialLoginRequest;
import buildup.server.member.exception.MemberErrorCode;
import buildup.server.member.exception.MemberException;
import buildup.server.member.repository.MemberRepository;
import io.jsonwebtoken.Claims;
import jakarta.validation.Valid;
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
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class MemberService {
    private final MemberRepository memberRepository;
    private final AuthenticationManager authenticationManager;
    private final AuthTokenProvider tokenProvider;
    private final AppProperties appProperties;
    private final RefreshTokenRepository refreshTokenRepository;

    private static final String SOCIAL_PW = "social1234";
    private static final long THREE_DAYS_MSEC = 259200000;

    //TODO: 추후 제거
    @Transactional
    public String test() {
        Member currentMember = findCurrentMember();
        return "인증정보="+currentMember.getUsername();
    }

    private Member findCurrentMember() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        Member user = memberRepository.findByUsername(authentication.getName()).get();
        return user;
    }

    // TODO: 추후 제거

    // 일반 회원가입 후 자동 로그인
    @Transactional
    public AuthInfo join(@Valid LocalJoinRequest request) {
        if (memberRepository.findByUsername(request.getUsername()).isPresent())
            throw new MemberException(MemberErrorCode.MEMBER_DUPLICATED);

        // 신규 회원이면 멤버 엔티티 db에 저장
        saveMember(request);

        // 회원 가입 후 자동 로그인
        LoginRequest loginRequest = LoginRequest.toLoginRequest(request);
        return new AuthInfo(
                createAuth(loginRequest),
                setRefreshToken(loginRequest)
        );
    }

    // 일반 로그인
    @Transactional
    public AuthInfo signIn(LoginRequest request) {
        // 회원이 가입되어 있는지 확인
        memberRepository.findByUsername(request.getUsername())
                .orElseThrow(() -> new MemberException(MemberErrorCode.MEMBER_NOT_FOUND));

        //로그인
        return new AuthInfo(
                    createAuth(request),
                    setRefreshToken(request)
        );
    }

    @Transactional
    public AuthInfo signIn(SocialLoginRequest request) {
        String username = request.getProvider() + request.getEmail();
        if (memberRepository.findByUsername(username).isEmpty()) {
            // 멤버 디비에 저장 = 회원 가입
            saveMember(request, SOCIAL_PW);
        }
        LoginRequest loginRequest = LoginRequest.toLoginRequest(request, SOCIAL_PW);
        return new AuthInfo(
                createAuth(loginRequest),
                setRefreshToken(loginRequest)
        );

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

        // 토큰 만료기간이 3일 이하인 경우 refresh token 발급
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

    //TODO: 리팩토링-중복코드 제거

    // 액세스 토큰 발급
    private AuthToken createAuth(LoginRequest request) {

        // TODO: BadCredentialsException 처리 (아이디, 비밀번호 틀린 경우)
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
                    ((CustomUserDetails) authentication.getPrincipal()).getRole().getKey(),
                    new Date(now.getTime() + appProperties.getAuth().getTokenExpiry())
            );

            return accessToken;
        } catch (BadCredentialsException e) {
            throw new AuthException(AuthErrorCode.CREDENTIAL_MISS_MATCH);
        }
    }

    // 리프레시 토큰 발급 및 저장 또는 수정
    private MemberRefreshToken setRefreshToken(LoginRequest request) {

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

    private Member saveMember(LocalJoinRequest request) {
        return memberRepository.save(request.toEntity());
    }

    private Member saveMember(SocialLoginRequest request, String pw) {
        return memberRepository.save(
                SocialLoginRequest.toEntity(request, pw)
        );
    }

    public List<Member> findAll() {
        return memberRepository.findAll();
    }

}
