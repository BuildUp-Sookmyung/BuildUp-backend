package buildup.server.member.dto;

import buildup.server.member.domain.Profile;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
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

    public Profile toProfile() {
        return Profile.builder()
                .nickname(nickname)
                .email(email)
                .school(school)
                .grade(grade)
                .major(major)
                .build();
    }

}
