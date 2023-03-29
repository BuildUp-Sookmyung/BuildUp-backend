package buildup.server.activity.dto;

import buildup.server.activity.domain.Activity;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ActivityListResponse {

    private Long activityId;
    private String activityName;
    private String categoryName;
    private LocalDate startDate;
    private LocalDate endDate;

    private Integer percentage;

    public ActivityListResponse(Long activityId, String activityName, String categoryName, LocalDate startDate, LocalDate endDate, Integer percentage) {
        this.activityId = activityId;
        this.activityName = activityName;
        this.categoryName = categoryName;
        this.startDate = startDate;
        this.endDate = endDate;
        this.percentage = percentage;
    }
}
