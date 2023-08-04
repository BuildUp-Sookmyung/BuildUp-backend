package buildup.server.common;

import buildup.server.member.domain.Member;
import buildup.server.member.domain.Provider;
import buildup.server.member.domain.Role;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

public class DummyObject {

    public static Member newMember(String username, String email, String password, Provider provider, Role role, String emailAgreeYn) {
        BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

        return Member.builder()
                .username(username)
                .email(email)
                .emailAgreeYn(emailAgreeYn)
                .password(passwordEncoder.encode(password))
                .provider(provider)
                .build();
    }
}
