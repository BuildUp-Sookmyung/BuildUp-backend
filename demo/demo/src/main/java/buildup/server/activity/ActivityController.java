package buildup.server.activity;

import buildup.server.activity.dto.*;
import buildup.server.activity.service.ActivityService;
import buildup.server.category.dto.CategorySaveRequest;
import buildup.server.common.response.StringResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/activities")
@RequiredArgsConstructor
public class ActivityController {

    private final ActivityService activityService;

    /**
     * 활동 기록 생성
     * */
    @PostMapping
    public StringResponse createActivity(@Valid @RequestPart ActivitySaveRequest request, @RequestPart MultipartFile img) {
        Long id = activityService.createActivity(request, img);
        return new StringResponse("활동을 생성했습니다. id: " + id);
    }

    /**
     * 활동 기록 조회
     * */
    // 기록-메인(전체)
    @GetMapping("/me")
    public List<ActivityListResponse> listMyActivities() {
        return activityService.readMyActivities();
    }

    // 기록-메인(카테고리별)
    @GetMapping("/me/{categoryId}")
    public List<ActivityListResponse> listMyActivitiesByCategory(@PathVariable Long categoryId) {
        return activityService.readMyActivitiesByCategory(categoryId);
    }

    // 홈 - 기록 필터링
    @PostMapping("/filter")
    public List<SearchResult> listMyActivitiesByFilter(@Valid @RequestBody FilterVO filter) {
        return activityService.readActivitiesByFilter(filter);
    }

    // 활동 상세
    @GetMapping("/{activityId}")
    public ActivityResponse readActivity(@PathVariable Long activityId) {
        return activityService.readOneActivity(activityId);
    }

    // 프로필 검색 상세
    @GetMapping("/profiles/{profileId}")
    public List<SearchResult> filterActivityByCategory(@PathVariable Long profileId) {
        return activityService.readActivitiesByProfile(profileId);
    }

    /**
     * 활동 기록 수정
     * */
    @PutMapping
    public StringResponse updateActivity(@Valid @RequestBody ActivityUpdateRequest requestDto) {
        activityService.updateActivities(requestDto);
        return new StringResponse("활동 수정 완료되었습니다");
    }

    @PutMapping("/img")
    public StringResponse updateActivityImg(@Valid @RequestPart ActivityImageUpdateRequest request, @RequestPart MultipartFile img) {
        activityService.updateActivityImages(request, img);
        return new StringResponse("활동 이미지 수정 완료되었습니다");
    }

    /**
     * 활동 기록 삭제
     * */
    @DeleteMapping("/{id}")
    public StringResponse deleteActivity(@PathVariable Long id) {
        activityService.deleteActivity(id);
        return new StringResponse("선택 항목 삭제 완료했습니다.");
    }
}
