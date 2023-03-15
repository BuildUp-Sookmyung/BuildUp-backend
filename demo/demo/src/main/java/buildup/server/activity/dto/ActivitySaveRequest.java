package buildup.server.activity.dto;

import buildup.server.activity.domain.Activity;
import buildup.server.category.Category;
import buildup.server.member.domain.Profile;
import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class ActivitySaveRequest {

    @NotNull
    private Long categoryId;

    @NotBlank
    private String activityName;

    private String hostName;

    private String roleName;

    private String urlName;

    @NotNull
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate startDate;

    @NotNull
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate endDate;


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
