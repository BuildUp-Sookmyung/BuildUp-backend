package buildup.server.member;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Entity
@Table(name = "phone")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Phone {

    @Id @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @OneToOne
    @JoinColumn(name = "member_id")
    private Member member;

    @Column(name = "phone_number")
    private String number;

    public Phone(Member member, String number) {
        this.member = member;
        this.number = number;
    }
}
