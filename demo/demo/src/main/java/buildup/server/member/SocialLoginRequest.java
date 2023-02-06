package buildup.server.member;

import jakarta.validation.constraints.NotBlank;
import lombok.AccessLevel;
import lombok.Builder;
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

    public SocialLoginRequest(String provider, String email, String phone) {
        this.provider = provider;
        this.email = email;
        this.phone = phone != null? phone : "NA";
    }

    public static Member toEntity(SocialLoginRequest request, String pw) {
        return Member.builder()
                .username(request.getProvider()+request.getEmail())
                .password(PasswordEncoderFactories.createDelegatingPasswordEncoder().encode(pw))
                .provider(Provider.toProvider(request.getProvider()))
                .profileSetYn("N")
                .build();
    }
}
