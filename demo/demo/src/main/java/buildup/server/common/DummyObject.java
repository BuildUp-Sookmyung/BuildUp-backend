package buildup.server.common;

import buildup.server.member.domain.Member;
import buildup.server.member.domain.Provider;
import buildup.server.member.domain.Role;
import buildup.server.member.dto.LocalJoinRequest;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

public class DummyObject {

    public static Member newMember(LocalJoinRequest request) {
        BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

        return Member.builder()
                .username(request.getUsername())
                .email(request.getProfile().getEmail())
                .emailAgreeYn(request.getEmailAgreeYn())
                .password(passwordEncoder.encode(request.getPassword()))
                .provider(Provider.LOCAL)
                .build();
    }
}
