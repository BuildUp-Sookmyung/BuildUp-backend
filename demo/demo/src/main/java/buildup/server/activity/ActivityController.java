package buildup.server.activity;

import buildup.server.activity.domain.Activity;
import buildup.server.activity.dto.ActivityRequest;
import buildup.server.activity.dto.ActivityResponse;
import buildup.server.activity.dto.ActivityUpdateRequest;
import buildup.server.activity.service.ActivityService;
import buildup.server.category.dto.CategoryResponse;
import buildup.server.category.dto.CategorySaveRequest;
import buildup.server.category.dto.CategoryUpdateRequest;
import buildup.server.common.response.StringResponse;
import buildup.server.member.dto.ProfileHomeResponse;
import buildup.server.member.dto.ProfilePageResponse;
import buildup.server.member.dto.ProfileSaveRequest;
import buildup.server.member.service.ProfileService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequestMapping("/activites")
@RequiredArgsConstructor
public class ActivityController {

    private final ActivityService activityService;

    @PostMapping("")
    public StringResponse createActivity(@RequestBody ActivityRequest requestdto) {
        Long id = activityService.createActivity(requestdto).getId();
        return new StringResponse("활동을 생성했습니다. id: " + id);
    }
    @GetMapping
    public List<ActivityResponse> listActivities() {
        return activityService.readActivities();
    }

    @PutMapping
    public StringResponse updateActivity(@RequestBody ActivityUpdateRequest requestdto) {
        activityService.updateActivity(requestdto);
        return new StringResponse("활동 수정 완료되었습니다");
    }

    @DeleteMapping("/{id}")
    public StringResponse deleteActivity(@PathVariable Long id) {
        activityService.deleteActivityById(id);
        return new StringResponse("선택 항목 삭제 완료했습니다.");
    }
}
