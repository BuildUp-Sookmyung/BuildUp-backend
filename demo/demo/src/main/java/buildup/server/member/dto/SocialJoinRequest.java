package buildup.server.member.dto;

import buildup.server.member.domain.Member;
import buildup.server.member.domain.Provider;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;

@Getter
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class SocialJoinRequest {

    private String provider;
    private ProfileSaveRequest profile;
    private String emailAgreeYn;
    private String smsAgreeYn;

    public static Member toEntity(SocialJoinRequest request, String pw) {
        return Member.builder()
                .username(request.getProvider()+request.profile.getEmail())
                .password(PasswordEncoderFactories.createDelegatingPasswordEncoder().encode(pw))
                .provider(Provider.toProvider(request.provider))
                .emailAgreeYn(request.emailAgreeYn)
                .smsAgreeYn(request.smsAgreeYn)
                .build();
    }
}
