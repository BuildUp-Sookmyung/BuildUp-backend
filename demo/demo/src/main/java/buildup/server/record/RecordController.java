package buildup.server.record;

import buildup.server.activity.dto.ActivitySaveRequest;
import buildup.server.common.response.StringResponse;
import buildup.server.member.service.S3Service;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/records")
public class RecordController {

    private final RecordService recordService;

    private final S3Service s3Service;

    @PostMapping
    public StringResponse createRecord(@RequestPart RecordSaveRequest request, @RequestPart List<MultipartFile> multipartFiles) {
        List<String> imgUrls = s3Service.uploadRecord(multipartFiles);
        System.out.println(imgUrls);
        Long id = recordService.createRecord(request, imgUrls);
        return new StringResponse("기록을 생성했습니다. id: " + id);
    }


}
