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
    private String categoryName;

    private String activityName;

    private String hostName;

    private String activityimg;

    private String roleName;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd", timezone = "Asia/Seoul")
    private LocalDate startDate;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd", timezone = "Asia/Seoul")
    private LocalDate endDate;
    private String urlName;

//    public static List<ActivityResponse> toDtoActList(List<Activity> entities) {
//        List<ActivityResponse> dtos = new ArrayList<>();
//
//        for (Activity entity : entities)
//            dtos.add(new ActivityResponse(entity.getId(),entity.getCategory().getName(), entity.getName(), entity.getHost(),
//                    entity.getActivity_img(), entity.getRole(), entity.getStartDate(), entity.getEndDate(), entity.getUrl()));
//
//        return dtos;
//    }

    public static ActivityResponse toDtoActList(Activity activity) {
        return new ActivityResponse(
                activity.getId(),
                activity.getCategory().getName(),
                activity.getName(),
                activity.getHost(),
                activity.getActivityimg(),
                activity.getRole(),
                activity.getStartDate(),
                activity.getEndDate(),
                activity.getUrl()
        );
    }
}
