package buildup.server.activity.dto;

import buildup.server.activity.domain.Activity;
import buildup.server.category.Category;
import buildup.server.member.domain.Member;
import buildup.server.member.domain.Profile;
import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;

@Getter
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

    @Builder
    public ActivitySaveRequest(Long categoryId, String activityName, String hostName, String roleName, String urlName, LocalDate startDate, LocalDate endDate) {
        this.categoryId = categoryId;
        this.activityName = activityName;
        this.hostName = hostName;
        this.roleName = roleName;
        this.urlName = urlName;
        this.startDate = startDate;
        this.endDate = endDate;
    }

    public Activity toActivity(Member member, Category category) {
        return Activity.builder()
                .name(activityName)
                .host(hostName)
                .role(roleName)
                .url(urlName)
                .startDate(startDate)
                .endDate(endDate)
                .member(member)
                .category(category)
                .build();
    }

}
