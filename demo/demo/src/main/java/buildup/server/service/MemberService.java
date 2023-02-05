package buildup.server.service;

import buildup.server.auth.service.AuthService;
import buildup.server.domain.user.Member;
import buildup.server.domain.user.Provider;
import buildup.server.dto.LocalJoinRequest;
import buildup.server.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class MemberService {
    private final MemberRepository memberRepository;
    private final AuthService authService;

    // 회원가입
    @Transactional
    public Long join(LocalJoinRequest request) {
        if (memberRepository.findByUsername(request.getUsername()).isEmpty()) {
            Member saveMember = saveMember(request);
            authService.createAuth(request);
            return saveMember.getId();
        }
        throw new RuntimeException();
    }

    private Member saveMember(LocalJoinRequest request) {
        return memberRepository.save(request.toEntity());
    }

    public List<Member> findAll() {
        return memberRepository.findAll();
    }

}
