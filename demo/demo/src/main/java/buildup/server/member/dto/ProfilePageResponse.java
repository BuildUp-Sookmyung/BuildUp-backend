package buildup.server.member.dto;

import buildup.server.entity.Interest;
import buildup.server.member.domain.Profile;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.stream.Collectors;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class ProfilePageResponse {

    private String nickname;
    private String email;
    private String school;
    private String major;
    private String grade;
    private String profileImg;
    private List<String> interests;
    private String schoolPublicYn;

    public static ProfilePageResponse of(Profile profile) {
        List<String> fieldList = profile.getInterests().stream()
                .map(Interest::getField).collect(Collectors.toList());
        return new ProfilePageResponse(
                profile.getNickname(),
                profile.getEmail(),
                profile.getSchool(),
                profile.getMajor(),
                profile.getGrade(),
                profile.getImgUrl(),
                fieldList,
                profile.getSchoolPublicYn()
        );
    }
}
