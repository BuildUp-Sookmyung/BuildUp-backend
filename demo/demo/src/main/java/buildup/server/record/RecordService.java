package buildup.server.record;

import buildup.server.activity.domain.Activity;
import buildup.server.activity.dto.ActivityListResponse;
import buildup.server.activity.dto.ActivityResponse;
import buildup.server.activity.dto.ActivitySaveRequest;
import buildup.server.activity.exception.ActivityErrorCode;
import buildup.server.activity.exception.ActivityException;
import buildup.server.activity.repository.ActivityRepository;
import buildup.server.category.Category;
import buildup.server.category.CategoryRepository;
import buildup.server.category.CategoryService;
import buildup.server.category.dto.CategoryResponse;
import buildup.server.category.exception.CategoryErrorCode;
import buildup.server.category.exception.CategoryException;
import buildup.server.member.domain.Member;
import buildup.server.member.repository.MemberRepository;
import buildup.server.member.service.MemberService;
import buildup.server.member.service.S3Service;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class RecordService {
    private final ActivityRepository activityRepository;
    private final MemberRepository memberRepository;
    private final RecordRepository recordRepository;
    private final RecordImgRepository recordImgRepository;
    private final MemberService memberService;
    private final CategoryRepository categoryRepository;
    private final CategoryService categoryService;
    private final S3Service s3Service;

    @Transactional
    public Long createRecord(RecordSaveRequest requestDto, List<String> imgUrls) {
        recordBlankCheck(imgUrls);

//        Member member = memberService.findCurrentMember();

        Record record = requestDto.toRecord();
//        recordRepository.save(record);

        List<String> images = new ArrayList<>();

        for (String imgUrl : imgUrls) {
            RecordImg recordImg = new RecordImg(imgUrl, record);
            recordImgRepository.save(recordImg);
            images.add(recordImg.getStoreUrl());
        }
        return recordRepository.save(record).getId();
    }
    @Transactional(readOnly = true)
    public RecordResponse readOneRecord(Long recordId) {
        Record record = recordRepository.findById(recordId)
                .orElseThrow(() -> new RecordException(RecordErrorCode.NOT_FOUND_RECORD));

        List<String> imgUrls = recordImgRepository.findAllByRecord(record)
                .stream()
                .map(RecordImg::getStoreUrl)
                .collect(Collectors.toList());

        return new RecordResponse(recordId, record, imgUrls);
    }

    @Transactional(readOnly = true)
    public List<RecordListResponse> readAllRecordByActivity(Long activityId) {

        Activity activity = activityRepository.findById(activityId)
                .orElseThrow(() -> new ActivityException(ActivityErrorCode.ACTIVITY_NOT_FOUND));
        List<Record> records = recordRepository.findAll();
        records.addAll(recordRepository.findAllByActivity(activity));
        return RecordListResponse.toDtoList(records);

    }

    private void recordBlankCheck(List<String> imgUrls) {
        if(imgUrls == null || imgUrls.isEmpty()){
            throw new RecordException(RecordErrorCode.WRONG_INPUT_IMAGE);
        }
    }

}
