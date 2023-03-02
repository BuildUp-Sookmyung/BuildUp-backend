package buildup.server.record;

import buildup.server.activity.dto.ActivityListResponse;
import buildup.server.activity.dto.ActivityResponse;
import buildup.server.activity.dto.ActivitySaveRequest;
import buildup.server.activity.dto.ActivityUpdateRequest;
import buildup.server.category.Category;
import buildup.server.common.response.StringResponse;
import buildup.server.member.domain.Member;
import buildup.server.member.service.S3Service;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Map;


@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/records")
public class RecordController {

    private final RecordService recordService;

    private final S3Service s3Service;


    @PostMapping
    public StringResponse createRecord(@RequestPart RecordSaveRequest request, @RequestPart(required=false) List<MultipartFile> multipartFiles) {
        if (multipartFiles == null) {
            throw new RecordException(RecordErrorCode.WRONG_INPUT_CONTENT);
        }
        List<String> imgUrls = s3Service.uploadRecord(multipartFiles);
        Long id = recordService.createRecord(request, imgUrls);
        return new StringResponse("기록을 생성했습니다. id: " + id);
    }

    @GetMapping("/{recordId}")
    public RecordResponse readoneRecord(@PathVariable Long recordId) {
        return recordService.readOneRecord(recordId);
    }

    @GetMapping("/activity/{activityId}")
    public List<RecordListResponse> readAllRecordByActivity(@PathVariable Long activityId){
        return recordService.readAllRecordByActivity(activityId);
    }

    @PutMapping
    public StringResponse updateRecord(@Valid @RequestBody RecordUpdateRequest requestDto) {
        recordService.updateRecords(requestDto);
        return new StringResponse("기록 수정 완료되었습니다");
    }

    @PutMapping("/imgs")
    public StringResponse updateRecordImg(@RequestPart RecordImageUpdateRequest request, @RequestPart(required=false) List<MultipartFile> multipartFiles) {
        recordService.updateRecordImages(request, multipartFiles);
        return new StringResponse("기록 이미지 수정이 완료되었습니다");
    }

    @DeleteMapping("/{id}")
    public StringResponse deleteRecord(@PathVariable Long id) {
        recordService.deleteRecord(id);
        return new StringResponse("선택 기록 삭제 완료했습니다.");
    }




}
