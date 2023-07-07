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

    // TODO: 프론트랑 얘기해보기
    private String activityimg;

    private String roleName;

    private LocalDate startDate;

    private LocalDate endDate;
    private String urlName;

//    private Integer percentage;

}
