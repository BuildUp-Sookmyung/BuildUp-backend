package buildup.server.dto;


import buildup.server.domain.user.Member;
import lombok.Data;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.time.LocalDate;

@Data
public class LocalJoinRequest {
    private final PasswordEncoder passwordEncoder;
    private String username;
    private String password;
    private String phone;

    public Member toEntity() {
        return Member.builder()
                .username(username)
                .password(passwordEncoder.encode(password))
                .build();
    }

}