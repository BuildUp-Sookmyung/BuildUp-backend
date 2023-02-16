package buildup.server.member.dto;

import buildup.server.entity.Interest;
import buildup.server.member.domain.Profile;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class InterestsSaveRequest {

    private List<String> interests;

    public static List<Interest> saveInterests(InterestsSaveRequest list, Profile profile) {
        for (String interest : list.interests) {
            profile.getInterestList().add(new Interest(profile, interest));
        }
        return profile.getInterestList();
    }
}
