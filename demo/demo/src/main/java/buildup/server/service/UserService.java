package buildup.server.service;
import buildup.buildupback.domain.user.User;
import buildup.buildupback.dto.CreateLocalAccountRequestDto;
import buildup.buildupback.repository.UserRepository;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;
import java.util.NoSuchElementException;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;

    public User findById(Long id) {
        return userRepository.findById(id)
                .orElseThrow(() -> new NoSuchElementException("해당 회원 정보가 존재하지 않습니다."));
    }

    public User findByUsername(String username) {
        return userRepository.findByUsername(username)
                .orElseThrow(() -> new NoSuchElementException("해당 회원 정보가 존재하지 않습니다."));
    }

    public List<User> findAll() {
        return userRepository.findAll();
    }

    // 회원가입
    @Transactional
    public Long join(User generatedUser) {
        userRepository.save(generatedUser);
        return generatedUser.getId();
    }

}
