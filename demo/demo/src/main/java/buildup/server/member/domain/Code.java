package buildup.server.member.domain;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Entity
@Table(name = "code")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Code {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private String email;
    private String code;

    public Code(String email, String code) {
        this.email = email;
        this.code = code;
    }
}
