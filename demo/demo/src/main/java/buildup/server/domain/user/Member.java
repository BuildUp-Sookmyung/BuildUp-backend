package buildup.server.domain.user;

import buildup.server.common.BaseTimeEntity;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
public class Member extends BaseTimeEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String nickname;

    @Column(nullable = false)
    private String email;

    @Column(nullable = false)
    private String username;

    @Column(nullable = false)
    private String password;

    @Enumerated(EnumType.STRING) // DB 저장 시 Enum 값 설정 (디폴트 = int형 숫자) -> 문자로 변경
    @Column(nullable = false)
    private Role role;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Provider provider;

    @Column(nullable = false)
    private String smsAgreeYn;

    @Column(nullable = false)
    private String emailAgreeYn;

    @Setter
    @Column(nullable = false)
    private String profileSetYn;

    @Builder
    public Member(String nickname, String email, String username, String password, Provider provider, String smsAgreeYn, String emailAgreeYn) {
        this.nickname = nickname;
        this.email = email;
        this.username = username;
        this.password = password;
        this.role = Role.USER;
        this.provider = provider;
        this.smsAgreeYn = smsAgreeYn;
        this.emailAgreeYn = emailAgreeYn;
        this.profileSetYn = "N";
    }

    public String getRoleKey() {
        return this.role.getKey();
    }
}
