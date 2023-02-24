package buildup.server.member.domain;

import buildup.server.entity.Interest;
import buildup.server.member.domain.Member;
import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
public class Profile {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "profile_id")
    private Long id;

    @Setter
    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    private Member member;

    private String nickname;
    private String email;
    private String school;
    private String major;
    private String grade;

    @Setter
    private String imgUrl;

    private String schoolPublicYn;

    @OneToMany(mappedBy = "profile")
    private List<Interest> interests = new ArrayList<>();

    @Builder
    public Profile(Member member, String nickname, String email, String school, String major, String grade, String schoolPublicYn) {
        this.member = member;
        this.nickname = nickname;
        this.email = email;
        this.school = school;
        this.major = major;
        this.grade = grade;
        this.schoolPublicYn = schoolPublicYn;
    }

    public Profile updateProfile(String nickname, String school, String major, String grade, String schoolPublicYn) {
        this.nickname = nickname;
        this.school = school;
        this.major = major;
        this.grade = grade;
        this.schoolPublicYn = schoolPublicYn;

        return this;
    }
}
