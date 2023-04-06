package buildup.server.record.controller;

import buildup.server.common.response.StringResponse;
import buildup.server.member.service.S3Service;
import buildup.server.record.dto.*;
import buildup.server.record.exception.RecordErrorCode;
import buildup.server.record.exception.RecordException;
import buildup.server.record.service.RecordService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;


@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/records")
public class RecordController {

    private final RecordService recordService;

    private final S3Service s3Service;


    @PostMapping
    public StringResponse createRecord(@RequestPart RecordSaveRequest request, @RequestPart(required=false) List<MultipartFile> multipartFiles) {
        Long id = recordService.createRecord(request, multipartFiles);
        return new StringResponse("기록을 생성했습니다. id: " + id);
    }

    @GetMapping("/{recordId}")
    public RecordResponse readOneRecord(@PathVariable Long recordId) {
        return recordService.readOneRecord(recordId);
    }

    @GetMapping("/activities/{activityId}")
    public List<RecordListResponse> readAllRecordsByActivity(@PathVariable Long activityId){
        return recordService.readAllRecordByActivity(activityId);
    }

    @PutMapping
    public StringResponse updateRecord(@Valid @RequestBody RecordUpdateRequest requestDto) {
        recordService.updateRecords(requestDto);
        return new StringResponse("기록 수정 완료되었습니다");
    }
    @PutMapping("/imgs")
    public StringResponse updateRecordImg(@RequestPart RecordImageUpdateRequest request, @RequestPart(required=false) List<MultipartFile> multipartFiles) {
        recordService.updateRecordImage(request, multipartFiles);
        return new StringResponse("기록 이미지 수정이 완료되었습니다");
    }

    @DeleteMapping
    public StringResponse deleteRecords(@RequestParam List<String> id) {
        recordService.deleteRecords(id);
        return new StringResponse("선택 기록 삭제 완료했습니다.");
    }





}
