package buildup.server.activity.dto;

import buildup.server.activity.domain.Activity;
import buildup.server.category.Category;
import buildup.server.category.dto.CategoryResponse;
import buildup.server.entity.Interest;
import buildup.server.member.domain.Profile;
import buildup.server.member.dto.ProfileHomeResponse;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Getter
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ActivityResponse {

    private Long activityId;
//    private String categoryName;

    private String activityName;

    private String hostName;

    private String activityimg;

    private String roleName;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd", timezone = "Asia/Seoul")
    private LocalDate startDate;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd", timezone = "Asia/Seoul")
    private LocalDate endDate;
    private String urlName;

    private String percentage;


//    public static List<ActivityResponse> toDtoActList(List<ActivityResponse> activity) {
//        return new ActivityResponse(
//                activity.getId(),
//                activity.getName(),
//                activity.getHost(),
//                activity.getActivityimg(),
//                activity.getRole(),
//                activity.getStartDate(),
//                activity.getEndDate(),
//                activity.getUrl(),
//                activity.getPercentage()
//        );
//    }

    public static List<ActivityResponse> toDtoList(List<Activity> entities) {
        List<ActivityResponse> dtos = new ArrayList<>();

        for (Activity entity : entities)
            dtos.add(new ActivityResponse(entity.getId(),entity.getName(),entity.getHost(),
                    entity.getActivityimg(),entity.getRole(),entity.getStartDate(), entity.getEndDate(),
                    entity.getUrl(),entity.getPercentage()));

        return dtos;
    }


}
