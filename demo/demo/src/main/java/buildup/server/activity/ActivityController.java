package buildup.server.activity;

import buildup.server.activity.dto.ActivityListResponse;
import buildup.server.activity.dto.ActivityResponse;
import buildup.server.activity.dto.ActivitySaveRequest;
import buildup.server.activity.dto.ActivityUpdateRequest;
import buildup.server.activity.service.ActivityService;
import buildup.server.category.dto.CategorySaveRequest;
import buildup.server.common.response.StringResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequestMapping("/activities")
@RequiredArgsConstructor
public class ActivityController {

    private final ActivityService activityService;

    @PostMapping
    public StringResponse createActivity(@Valid @RequestPart ActivitySaveRequest request, @RequestPart MultipartFile img) {
        Long id = activityService.createActivity(request, img);
        return new StringResponse("활동을 생성했습니다. id: " + id);
    }
    @GetMapping
    public List<ActivityListResponse> listMyActivities() {
        return activityService.readMyActivities();
    }

    @GetMapping("/category/{categoryId}")
    public List<ActivityListResponse> listMyActivitiesByCategory(@PathVariable Long categoryId) {
        return activityService.readMyActivitiesByCategory(categoryId);
    }

    @GetMapping("/{activityId}")
    public ActivityResponse readActivity(@PathVariable Long activityId) {
        return activityService.readOneActivity(activityId);
    }

    @PutMapping
    public StringResponse updateActivity(@Valid @RequestBody ActivityUpdateRequest requestDto) {
        activityService.updateActivities(requestDto);
        return new StringResponse("활동 수정 완료되었습니다");
    }

    @PutMapping("/img")
    public StringResponse updateActivityImg(MultipartFile img) {
        activityService.updateActivityImages(img);
        return new StringResponse("활동 이미지 수정 완료되었습니다");
    }

    @DeleteMapping("/{id}")
    public StringResponse deleteActivity(@PathVariable Long id) {
        activityService.deleteActivity(id);
        return new StringResponse("선택 항목 삭제 완료했습니다.");
    }
}
