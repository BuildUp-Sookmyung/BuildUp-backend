package buildup.server.activity;

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
@RequestMapping("/activites")
@RequiredArgsConstructor
public class ActivityController {

    private final ActivityService activityService;

    @PostMapping
    public StringResponse createActivity(@Valid @RequestBody ActivitySaveRequest request, @RequestPart MultipartFile img) {
        Long id = activityService.createActivity(request, img);
        return new StringResponse("활동을 생성했습니다. id: " + id);
    }
    @GetMapping
    public List<ActivityResponse> listActivities() {
        return activityService.readActivities();
    }

    @PutMapping
    public StringResponse updateActivity(@RequestBody ActivityUpdateRequest requestdto) {
        activityService.updateActivityS(requestdto);
        return new StringResponse("활동 수정 완료되었습니다");
    }

    @PutMapping("/img")
    public StringResponse updateActivityImg(MultipartFile img) {
        activityService.updateActivityImageS(img);
        return new StringResponse("활동 이미지 수정 완료되었습니다");
    }

    @DeleteMapping("/{id}")
    public StringResponse deleteActivity(@PathVariable Long id) {
        activityService.deleteActivityS(id);
        return new StringResponse("선택 항목 삭제 완료했습니다.");
    }
}
