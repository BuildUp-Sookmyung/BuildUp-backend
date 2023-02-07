package buildup.server.member.dto;

import buildup.server.member.domain.Member;
import buildup.server.member.domain.Provider;
import jakarta.validation.constraints.NotBlank;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class SocialLoginRequest {

    @NotBlank
    private String provider;

    @NotBlank
    private String email;
    private String phone;

    public static Member toEntity(SocialLoginRequest request, String pw) {
        return Member.builder()
                .username(request.getProvider()+request.getEmail())
                .password(PasswordEncoderFactories.createDelegatingPasswordEncoder().encode(pw))
                .provider(Provider.toProvider(request.getProvider()))
                .profileSetYn("N")
                .build();
    }
}
