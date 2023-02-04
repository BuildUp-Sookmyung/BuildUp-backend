package buildup.server.dto;


import buildup.server.domain.user.Member;
import lombok.Data;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.time.LocalDate;

@Data
public class CreateLocalAccountRequestDto {
    private final BCryptPasswordEncoder passwordEncoder;
    private String username;
    private String password;
    private String nickname;
    private String phone;
    private String email;
    private LocalDate birth;

    public Member toEntity() {
        return Member.builder()
                .username(username)
                .password(passwordEncoder.encode(password))
                .nickname(nickname)
                .phone(phone)
                .email(email)
                .birth(birth)
                .build();
    }

}