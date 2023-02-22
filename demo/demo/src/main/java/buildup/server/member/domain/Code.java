package buildup.server.member.domain;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Entity
@Table(name = "code")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Code {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private String name;
    private String email;
    private String code;
    @Setter
    private String authYn;

    @Setter
    private String expiredYn;

    public Code(String name, String email, String code) {
        this.email = email;
        this.code = code;
        this.authYn = "N";
        this.expiredYn = "N";
    }
}
