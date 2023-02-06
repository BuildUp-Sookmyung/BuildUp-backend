package buildup.server.member;


import jakarta.validation.constraints.NotBlank;
import lombok.*;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class LocalJoinRequest {

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

    @NotBlank
    private String phone;

    public Member toEntity() {
        return Member.builder()
                .nickname(nickname)
                .username(username)
                .password(PasswordEncoderFactories.createDelegatingPasswordEncoder().encode(password))
                .provider(Provider.LOCAL)
                .smsAgreeYn(smsAgreeYn)
                .emailAgreeYn(emailAgreeYn)
                .profileSetYn("N")
                .build();
    }


}