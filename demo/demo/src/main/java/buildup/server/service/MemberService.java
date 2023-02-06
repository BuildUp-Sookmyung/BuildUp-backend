package buildup.server.service;

import buildup.server.auth.domain.AuthInfo;
import buildup.server.auth.service.AuthService;
import buildup.server.domain.member.Member;
import buildup.server.dto.LocalJoinRequest;
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
        if (memberRepository.findByUsername(request.getUsername()).isEmpty()) {
            // 신규 회원이면
            Member saveMember = saveMember(request);
            return new AuthInfo(
                    authService.createAuth(request),
                    authService.setRefreshToken(request)
            );
        }
        throw new RuntimeException("이미 가입된 사용자");
        //TODO: 예외처리
    }

    private Member saveMember(LocalJoinRequest request) {
        return memberRepository.save(request.toEntity());
    }

    public List<Member> findAll() {
        return memberRepository.findAll();
    }

}
