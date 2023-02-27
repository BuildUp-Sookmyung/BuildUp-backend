package buildup.server.activity.dto;

import buildup.server.activity.domain.Activity;
import buildup.server.member.domain.Profile;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class ActivityRequest {

    private String categoryName;

    private String activityName;

    private String hostName;

    private String roleName;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd", timezone = "Asia/Seoul")
    private LocalDate startDate;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd", timezone = "Asia/Seoul")
    private LocalDate endDate;
    private String urlName;

    public Activity toActivity() {
        return Activity.builder()
                .name(activityName)
                .host(hostName)
                .role(roleName)
                .startDate(startDate)
                .endDate(endDate)
                .build();
    }

}
