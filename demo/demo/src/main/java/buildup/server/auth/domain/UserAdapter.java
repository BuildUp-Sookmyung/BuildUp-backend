package buildup.server.auth.domain;

import buildup.server.member.domain.Member;
import lombok.Getter;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;

import java.util.List;

@Getter
public class UserAdapter extends User {

    private Member member;

    public UserAdapter(Member member) {
        super(member.getUsername(), member.getPassword(), List.of(new SimpleGrantedAuthority("ROLE_USER")));
        this.member = member;
    }

    public static UserAdapter create(Member member) {
        return new UserAdapter(member);
    }
}
