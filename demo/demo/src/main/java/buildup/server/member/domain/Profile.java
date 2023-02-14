package buildup.server.member.domain;

import buildup.server.entity.Interest;
import buildup.server.member.domain.Member;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
public class Profile {

    @Id @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "profile_id")
    private Long id;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    private Member member;

    private String nickname;
    private String email;
    private String school;
    private String major;
    private String grade;
    private String imgUrl;

    @OneToMany(mappedBy = "profile")
    private List<Interest> interestList = new ArrayList<>();

    @Builder
    public Profile(Member member, String nickname, String email, String school, String major, String grade, String imgUrl) {
        this.member = member;
        this.nickname = nickname;
        this.email = email;
        this.school = school;
        this.major = major;
        this.grade = grade;
        this.imgUrl = imgUrl;
    }
}
