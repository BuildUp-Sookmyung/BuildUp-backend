package buildup.server.member.dto;

import buildup.server.member.domain.Member;
import buildup.server.member.domain.Profile;
import jakarta.validation.constraints.NotBlank;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class ProfileSaveRequest {

    @NotBlank
    private String nickname;
    private String email;
    private String school;
    @NotBlank
    private String major;
    @NotBlank
    private String grade;
    private String schoolPublicYn;

    private List<String> interests;

    public Profile toProfile(Member member) {
        return Profile.builder()
                .nickname(nickname)
                .email(email)
                .school(school)
                .grade(grade)
                .major(major)
                .schoolPublicYn(schoolPublicYn)
                .member(member)
                .build();
    }

    public void updateProfile(Profile profile) {
        profile.updateProfile(this.nickname, this.school, this.major, this.grade, this.schoolPublicYn);
    }

}
