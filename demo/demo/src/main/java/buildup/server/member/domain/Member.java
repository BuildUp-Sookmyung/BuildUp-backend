package buildup.server.member.domain;

import buildup.server.common.BaseTimeEntity;
import jakarta.persistence.*;
import lombok.*;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
public class Member extends BaseTimeEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column
    private String username;

    @Column
    private String password;

    @Enumerated(EnumType.STRING) // DB 저장 시 Enum 값 설정 (디폴트 = int형 숫자) -> 문자로 변경
    @Column(nullable = false)
    private Role role;

    @Enumerated(EnumType.STRING)
    @Column
    private Provider provider;

    @Column
    private String smsAgreeYn;

    @Column
    private String emailAgreeYn;


    @Builder
    public Member(String username, String password, Provider provider, String smsAgreeYn, String emailAgreeYn) {
        this.username = username;
        this.password = password;
        this.role = Role.USER;
        this.provider = provider;
        this.smsAgreeYn = smsAgreeYn;
        this.emailAgreeYn = emailAgreeYn;
    }

    public String getRoleKey() {
        return this.role.getKey();
    }
}
