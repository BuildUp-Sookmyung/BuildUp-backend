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
public class ActivitySaveRequest {

    private String categoryName;

    private String activityName;

    private String hostName;

    private String roleName;

    private String urlName;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd", timezone = "Asia/Seoul")
    private LocalDate startDate;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd", timezone = "Asia/Seoul")
    private LocalDate endDate;


    private String percentage;

    public Activity toActivity() {
        return Activity.builder()
                .name(activityName)
                .host(hostName)
                .role(roleName)
                .url(urlName)
                .startDate(startDate)
                .endDate(endDate)
                .build();
    }

//    public Activity toActivity(){
//        return new Activity(categoryName, activityName, hostName, roleName, urlName,startDate, endDate, percentage);
//    }


}
