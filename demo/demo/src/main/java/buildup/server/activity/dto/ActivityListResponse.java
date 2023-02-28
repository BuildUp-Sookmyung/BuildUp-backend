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
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ActivityListResponse {

    private Long activityId;
    private String activityName;
    private LocalDate startDate;
    private LocalDate endDate;
//    private String percentage;

    public static List<ActivityListResponse> toDtoList(List<Activity> entities) {
        List<ActivityListResponse> dtos = new ArrayList<>();

        for (Activity entity : entities)
            dtos.add(new ActivityListResponse(entity.getId(),
                    entity.getName(),entity.getStartDate(), entity.getEndDate()));
        return dtos;
    }
}
