package buildup.server.record.service;

import buildup.server.activity.domain.Activity;
import buildup.server.activity.exception.ActivityErrorCode;
import buildup.server.activity.exception.ActivityException;
import buildup.server.activity.repository.ActivityRepository;
import buildup.server.category.CategoryRepository;
import buildup.server.category.CategoryService;
import buildup.server.member.domain.Member;
import buildup.server.member.repository.MemberRepository;
import buildup.server.member.service.MemberService;
import buildup.server.member.service.S3Service;
import buildup.server.record.dto.*;
import buildup.server.record.exception.RecordErrorCode;
import buildup.server.record.exception.RecordException;
import buildup.server.record.domain.Record;
import buildup.server.record.domain.RecordImg;
import buildup.server.record.repository.RecordImgRepository;
import buildup.server.record.repository.RecordRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
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
    public Long createRecord(RecordSaveRequest requestDto, List<MultipartFile> multipartFiles) {

        Activity activity = activityRepository.findById(requestDto.getActivityId())
                .orElseThrow(() -> new ActivityException(ActivityErrorCode.ACTIVITY_NOT_FOUND));
        Record record = requestDto.toRecord();
        record.setActivity(activity);

        if (multipartFiles == null) {throw new RecordException(RecordErrorCode.WRONG_INPUT_CONTENT);}

        recordRepository.save(record);

        List<String> imgUrls = s3Service.uploadRecord(multipartFiles);
        for (String imgUrl : imgUrls) {
            RecordImg recordImg = new RecordImg(imgUrl, record);
            recordImgRepository.save(recordImg);
        }
        return record.getId();
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
        return RecordListResponse.toDtoList(recordRepository.findAllByActivity(activity));

    }
    @Transactional
    public void updateRecords(RecordUpdateRequest requestDto) {
        Record record = recordRepository.findById(requestDto.getId())
                .orElseThrow(() -> new RecordException(RecordErrorCode.NOT_FOUND_RECORD));
        record.updateRecord(requestDto.getRecordTitle(), requestDto.getExperienceName(), requestDto.getConceptName(),
                requestDto.getResultName(), requestDto.getContent(), requestDto.getDate(), requestDto.getUrlName());
    }

    @Transactional
    public void updateRecordImage(RecordImageUpdateRequest requestDto, List<MultipartFile> multipartFiles){
        List<RecordImg> findrecordimg = recordImgRepository.findByRecordId(requestDto.getRecordid());
        List<String> imgUrls = s3Service.uploadRecord(multipartFiles);
        for(RecordImg recordImg : findrecordimg){
            String old_url = recordImg.getStoreUrl();
            String new_url = imgUrls.get(findrecordimg.indexOf(recordImg));
            if(old_url == null){
                recordImg.setStoreUrl(new_url);
            }else{
                s3Service.deleteOneRecord(old_url);
                recordImg.setStoreUrl(new_url);
            }
        }

    }

    @Transactional
    public void deleteRecord(Long id) {
        Record record = recordRepository.findById(id)
                .orElseThrow(() -> new RecordException(RecordErrorCode.NOT_FOUND_RECORD));
        List<RecordImg> deleterecordimg = recordImgRepository.findAllByRecord(record);
        recordImgRepository.deleteAll(deleterecordimg);
        recordRepository.delete(record);
    }

    private RecordImgRequest updateoneImage(MultipartFile multipartFile){
        return RecordImgRequest.builder()
                .storeUrl(s3Service.uploadOneRecord(multipartFile))
                .build();
    }

    private void putRequestParser(List<RecordImg> recordImgList, List<RecordImgRequest> recordImgRequestList){
        for (RecordImgRequest recordImgRequest : recordImgRequestList){
            RecordImg recordImg = RecordImg.builder()
                    .storeUrl(recordImgRequest.getStoreUrl())
                    .build();
            recordImgList.add(recordImg);
        }
    }



}
