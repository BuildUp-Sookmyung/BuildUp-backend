package buildup.server.member.dto;


import buildup.server.member.domain.Member;
import buildup.server.member.domain.Provider;
import jakarta.validation.constraints.NotBlank;
import lombok.*;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class LocalJoinRequest {

    @NotBlank
    private String name;
    @NotBlank
    private String username;
    @NotBlank
    private String password;

    @NotBlank
    private String smsAgreeYn;

    @NotBlank
    private String emailAgreeYn;

    // TODO: 화 Profile 집어넣기

    public Member toMember() {
        return Member.builder()
                .name(name)
                .username(username)
                .password(PasswordEncoderFactories.createDelegatingPasswordEncoder().encode(password))
                .provider(Provider.LOCAL)
                .smsAgreeYn(smsAgreeYn)
                .emailAgreeYn(emailAgreeYn)
                .profileSetYn("N")
                .build();
    }

    // TODO: 화 PROFILE BUILDER


}