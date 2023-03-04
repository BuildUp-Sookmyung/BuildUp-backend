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
    private String username;
    @NotBlank
    private String password;

    private ProfileSaveRequest profile;

    @NotBlank
    private String emailAgreeYn;

    public Member toMember() {
        return Member.builder()
                .username(username)
                .password(PasswordEncoderFactories.createDelegatingPasswordEncoder().encode(password))
                .email(profile.getEmail())
                .provider(Provider.LOCAL)
                .emailAgreeYn(emailAgreeYn)
                .build();
    }


}