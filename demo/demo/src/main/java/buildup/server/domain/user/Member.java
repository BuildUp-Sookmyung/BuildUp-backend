package buildup.server.domain.user;

import buildup.server.BaseTimeEntity;
import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Getter
@NoArgsConstructor
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

    @Column(nullable = false)
    private String phone;

    @Column(nullable = true)
    private LocalDate birth;

    @Column
    private String picture;

    @Enumerated(EnumType.STRING) // DB 저장 시 Enum 값 설정 (디폴트 = int형 숫자) -> 문자로 변경
    @Column(nullable = false)
    private Role role;

    @Builder
    public Member(String nickname, String email, String username, String password, String picture, String phone, LocalDate birth, Role role) {
        this.nickname = nickname;
        this.email = email;
        this.username = username;
        this.password = password;
        this.phone = phone;
        this.birth = birth;
        this.picture = picture;
        this.role = role;
    }

    public Member update(String nickname, String picture) {
        this.nickname = nickname;
        this.picture = picture;

        return this;
    }

    public String getRoleKey() {
        return this.role.getKey();
    }
}
