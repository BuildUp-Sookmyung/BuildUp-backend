package buildup.server.dto;


import buildup.server.domain.user.Member;
import jakarta.persistence.Column;
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

    @NotBlank
    private String smsAgreeYn;

    @NotBlank
    private String emailAgreeYn;
    private String phone;

    public Member toEntity() {
        return Member.builder()
                .nickname(nickname)
                .username(username)
                .password(passwordEncoder.encode(password))
                .smsAgreeYn(smsAgreeYn)
                .emailAgreeYn(emailAgreeYn)
                .profileSetYn("N")
                .build();
    }

}