package buildup.server.member;

import buildup.server.auth.domain.*;
import buildup.server.auth.repository.RefreshTokenRepository;
import buildup.server.common.AppProperties;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class MemberService {
    private final MemberRepository memberRepository;
    private final AuthenticationManager authenticationManager;
    private final AuthTokenProvider tokenProvider;
    private final AppProperties appProperties;
    private final RefreshTokenRepository refreshTokenRepository;

    private static final String SOCIAL_PW = "social1234";

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
            throw new RuntimeException("이미 가입된 사용자");
            //TODO: 예외처리

        // 신규 회원이면 멤버 엔티티 db에 저장
        Member saveMember = saveMember(request);

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
                .orElseThrow(() -> new RuntimeException("회원이 존재하지 않음"));

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
            Member saveMember = saveMember(request, SOCIAL_PW);
        }
        LoginRequest loginRequest = LoginRequest.toLoginRequest(request, SOCIAL_PW);
        return new AuthInfo(
                createAuth(loginRequest),
                setRefreshToken(loginRequest)
        );

    }

    //TODO: 리팩토링-중복코드 제거

    // 액세스 토큰 발급
    private AuthToken createAuth(LoginRequest request) {

        // TODO: BadCredentialsException 처리 (아이디, 비밀번호 틀림)
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
