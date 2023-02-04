package buildup.server.service;

import buildup.server.domain.user.Member;
import buildup.server.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.NoSuchElementException;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class MemberService {
    private final MemberRepository memberRepository;

    public Member findById(Long id) {
        return memberRepository.findById(id)
                .orElseThrow(() -> new NoSuchElementException("해당 회원 정보가 존재하지 않습니다."));
    }

    public Member findByUsername(String username) {
        return memberRepository.findByUsername(username)
                .orElseThrow(() -> new NoSuchElementException("해당 회원 정보가 존재하지 않습니다."));
    }

    public List<Member> findAll() {
        return memberRepository.findAll();
    }

    // 회원가입
    @Transactional
    public Long join(Member generatedMember) {
        memberRepository.save(generatedMember);
        return generatedMember.getId();
    }

}
