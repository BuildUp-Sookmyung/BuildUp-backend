package buildup.server.service;

import buildup.server.auth.domain.AuthInfo;
import buildup.server.auth.service.AuthService;
import buildup.server.domain.member.Member;
import buildup.server.dto.LocalJoinRequest;
import buildup.server.dto.LocalLoginRequest;
import buildup.server.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class MemberService {
    private final MemberRepository memberRepository;
    private final AuthService authService;

    // 회원가입
    @Transactional
    public AuthInfo join(LocalJoinRequest request) {
        if (memberRepository.findByUsername(request.getUsername()).isPresent())
            throw new RuntimeException("이미 가입된 사용자");
            //TODO: 예외처리

        // 신규 회원이면 멤버 엔티티 db에 저장
        Member saveMember = saveMember(request);

        // 회원 가입 후 자동 로그인
        LocalLoginRequest localLoginRequest = LocalJoinRequest.toLoginRequest(request);
        return new AuthInfo(
                authService.createAuth(localLoginRequest),
                authService.setRefreshToken(localLoginRequest)
        );
    }

    @Transactional
    public AuthInfo signIn(LocalLoginRequest request) {
        // 회원이 가입되어 있는지 확인
        memberRepository.findByUsername(request.getUsername())
                .orElseThrow(() -> new RuntimeException("회원이 존재하지 않음"));

        //로그인
        return new AuthInfo(
                    authService.createAuth(request),
                    authService.setRefreshToken(request)
        );
    }

    private Member saveMember(LocalJoinRequest request) {
        return memberRepository.save(request.toEntity());
    }

    public List<Member> findAll() {
        return memberRepository.findAll();
    }

}
