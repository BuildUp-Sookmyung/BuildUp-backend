package buildup.server.member.dto;

import buildup.server.entity.Interest;
import buildup.server.member.domain.Profile;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class ProfileHomeResponse implements Comparable<ProfileHomeResponse>{

    private Long profileId;
    private String nickname;
    private String school;
    private String major;
    private String grade;
    private String schoolPublicYn;
    private String profileImg;
    private List<String> interests;

    public static ProfileHomeResponse toDto(Profile profile) {
        return new ProfileHomeResponse(
                profile.getId(),
                profile.getNickname(),
                profile.getSchool(),
                profile.getMajor(),
                profile.getGrade(),
                profile.getSchoolPublicYn(),
                profile.getImgUrl(),
                profile.getInterests().stream()
                        .map(Interest::getField).collect(Collectors.toList())
        );
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof ProfileHomeResponse)) return false;
        ProfileHomeResponse that = (ProfileHomeResponse) o;
        return profileId.equals(that.profileId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(profileId);
    }


    @Override
    public int compareTo(ProfileHomeResponse profile) {
        if (nickname.compareTo(profile.getNickname())<0)
            return -1;
        else if (nickname.compareTo(profile.getNickname())>0)
            return 1;
        return 0;
    }
}
