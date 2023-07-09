package buildup.server.member.service;

import buildup.server.auth.domain.*;
import buildup.server.auth.service.AuthService;
import buildup.server.member.domain.Member;
import buildup.server.member.domain.Provider;
import buildup.server.member.dto.LocalJoinRequest;
import buildup.server.member.dto.LoginRequest;
import buildup.server.member.dto.SocialJoinRequest;
import buildup.server.member.dto.SocialLoginRequest;
import buildup.server.member.exception.MemberErrorCode;
import buildup.server.member.exception.MemberException;
import buildup.server.member.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class MemberService {
    private final MemberRepository memberRepository;
    private final AuthService authService;
    private final ProfileService profileService;
    private static final String SOCIAL_PW = "social1234";

    //TODO: 추후 제거
    @Transactional
    public String test() {
        Member currentMember = findCurrentMember();
        return "인증정보="+currentMember.getUsername();
    }

    // TODO: 로그인한 사용자
    public Member findCurrentMember() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        Member user = memberRepository.findByUsername(authentication.getName())
                .orElseThrow(() -> new MemberException(MemberErrorCode.MEMBER_NOT_FOUND));
        return user;
    }

    // 일반 회원가입 후 자동 로그인
    @Transactional
    public AuthInfo join(LocalJoinRequest request) throws IOException {
        // 기존 회원 확인
        if (memberRepository.findByUsername(request.getUsername()).isPresent())
            throw new MemberException(MemberErrorCode.MEMBER_DUPLICATED);

        List<Member> members = memberRepository.findAllByEmail(request.getProfile().getEmail());
        for (Member member: members) {
            if (member.getProvider() == Provider.LOCAL)
                throw new MemberException(MemberErrorCode.MEMBER_DUPLICATED);
        }

        // 신규 회원이면 멤버 엔티티 db에 저장, 프로필 저장
        Member saveMember = saveMember(request);
        profileService.saveProfile(request.getProfile(), saveMember);

        // 자동 로그인
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

    @Transactional(readOnly = true)
    public void verifyDuplicatedId(String username) {
        if (memberRepository.findByUsername(username).isPresent())
            throw new MemberException(MemberErrorCode.MEMBER_DUPLICATED);
    }

    // 기존 회원이면 true, 신규 회원이면 false 리턴
    @Transactional
    public boolean verifyMember(SocialLoginRequest request) {
        String username = request.getProvider() + request.getEmail();
        return memberRepository.findByUsername(username).isPresent();
    }

    @Transactional
    public AuthInfo join(SocialJoinRequest request) throws IOException {
        if (memberRepository.findByUsername(request.getProvider()+request.getProfile().getEmail()).isPresent())
            throw new MemberException(MemberErrorCode.MEMBER_DUPLICATED);
        Member saveMember = saveMember(request, SOCIAL_PW);
        profileService.saveProfile(request.getProfile(), saveMember);
        LoginRequest loginRequest = LoginRequest.toLoginRequest(request, SOCIAL_PW);
        return new AuthInfo(
                authService.createAuth(loginRequest),
                authService.setRefreshToken(loginRequest)
        );

    }

    @Transactional
    public AuthInfo signIn(SocialLoginRequest request) {
        LoginRequest loginRequest = LoginRequest.toLoginRequest(request, SOCIAL_PW);
        return new AuthInfo(
                authService.createAuth(loginRequest),
                authService.setRefreshToken(loginRequest)
        );
    }

    private Member saveMember(LocalJoinRequest request) {
        return memberRepository.save(request.toMember());
    }

    private Member saveMember(SocialJoinRequest request, String pw) {
        return memberRepository.save(
                SocialJoinRequest.toEntity(request, pw)
        );
    }





}
