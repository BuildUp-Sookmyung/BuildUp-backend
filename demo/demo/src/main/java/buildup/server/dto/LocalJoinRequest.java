package buildup.server.dto;


import buildup.server.domain.user.Member;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;
import org.springframework.security.crypto.password.PasswordEncoder;

@Data
public class LocalJoinRequest {
    private final PasswordEncoder passwordEncoder;

    @NotBlank
    private String nickname;
    @NotBlank
    private String username;
    @NotBlank
    private String password;
    private String phone;

    public Member toEntity() {
        return Member.builder()
                .nickname(nickname)
                .username(username)
                .password(passwordEncoder.encode(password))
                .build();
    }

}