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
public class ProfileHomeResponse {

    private String nickname;
    private String school;
    private String major;
    private String grade;
    private String schoolPublicYn;
    private String imgUrl;
    private List<String> interests;

    public static ProfileHomeResponse toDto(Profile profile) {
        List<String> fieldList = profile.getInterests().stream()
                .map(Interest::getField).collect(Collectors.toList());
        return new ProfileHomeResponse(
                profile.getNickname(),
                profile.getSchool(),
                profile.getMajor(),
                profile.getGrade(),
                profile.getSchoolPublicYn(),
                profile.getImgUrl(),
                fieldList
        );
    }

}
