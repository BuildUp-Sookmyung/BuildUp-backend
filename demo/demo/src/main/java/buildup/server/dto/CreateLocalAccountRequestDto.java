package buildup.server.dto;


import buildup.buildupback.domain.user.User;
import lombok.Data;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.util.Date;

@Data
public class CreateLocalAccountRequestDto {
    private final BCryptPasswordEncoder passwordEncoder;
    private String username;
    private String password;
    private String nickname;
    private String phone;
    private String email;
    private Date birth;

    public User toEntity() {
        return User.builder()
                .username(username)
                .password(passwordEncoder.encode(password))
                .nickname(nickname)
                .phone(phone)
                .email(email)
                .birth(birth)
                .build();
    }

}