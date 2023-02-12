package buildup.server.member.service;

import buildup.server.auth.dto.TokenDto;
import buildup.server.auth.exception.AuthErrorCode;
import buildup.server.auth.exception.AuthException;
import buildup.server.auth.domain.*;
import buildup.server.auth.repository.RefreshTokenRepository;
import buildup.server.auth.service.AuthService;
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
    private final AuthService authService;
    private static final String SOCIAL_PW = "social1234";

    //TODO: 추후 제거
    @Transactional
    public String test() {
        Member currentMember = findCurrentMember();
        return "인증정보="+currentMember.getUsername();
    }

    private Member findCurrentMember() {
        // TODO: 쿼리 날리지 않고 사용자 정보 가져오기
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        Member user = memberRepository.findByUsername(authentication.getName()).get();
        return user;
    }

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
                authService.createAuth(loginRequest),
                authService.setRefreshToken(loginRequest)
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
                authService.createAuth(request),
                authService.setRefreshToken(request)
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
                authService.createAuth(loginRequest),
                authService.setRefreshToken(loginRequest)
        );

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
