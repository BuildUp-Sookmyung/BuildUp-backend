package buildup.server.service;

import buildup.server.domain.user.Member;
import buildup.server.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.NoSuchElementException;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;

    public Member findById(Long id) {
        return userRepository.findById(id)
                .orElseThrow(() -> new NoSuchElementException("해당 회원 정보가 존재하지 않습니다."));
    }

    public Member findByUsername(String username) {
        return userRepository.findByUsername(username)
                .orElseThrow(() -> new NoSuchElementException("해당 회원 정보가 존재하지 않습니다."));
    }

    public List<Member> findAll() {
        return userRepository.findAll();
    }

    // 회원가입
    @Transactional
    public Long join(Member generatedMember) {
        userRepository.save(generatedMember);
        return generatedMember.getId();
    }

}
