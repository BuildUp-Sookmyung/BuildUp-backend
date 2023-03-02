package buildup.server.record;

import buildup.server.activity.domain.Activity;
import buildup.server.activity.dto.ActivitySaveRequest;
import buildup.server.activity.repository.ActivityRepository;
import buildup.server.category.Category;
import buildup.server.category.CategoryRepository;
import buildup.server.category.CategoryService;
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
import java.util.List;

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
//    @Transactional
//    public Long createRecord(RecordSaveRequest requestDto, List<MultipartFile> recordimgs) {
//        Member member = memberService.findCurrentMember();
//        Record record = requestDto.toRecord();
//
//        List<String> fileUrls = new ArrayList<>();
//
//        // 파일 업로드 갯수 3개 이하
//        for (MultipartFile multipartFile : recordimgs) {
//            if (fileUrls.size() > 3) {
//                throw new RecordException(RecordErrorCode.FILE_COUNT_EXCEED);
//            }
//
//
//        String record_url = null;
//        if (! img.isEmpty())
//            record_url = s3Service.uploadRecord(record, member.getId(), img);
//        activity.setMember(member);
//        activity.setActivityimg(activity_url);
//        return activityRepository.save(activity).getId();
//    }

    @Transactional
    public Long createRecord(RecordSaveRequest requestDto, List<String> imgUrls) {
        recordBlankCheck(imgUrls);

        Member member = memberService.findCurrentMember();

        Record record = requestDto.toRecord();
        recordRepository.save(record);

        List<String> images = new ArrayList<>();

        for (String imgUrl : imgUrls) {
            RecordImg recordImg = new RecordImg(imgUrl, record);
            recordImgRepository.save(recordImg);
            images.add(recordImg.getStoreUrl());
        }
        return recordRepository.save(record).getId();
    }
    private void recordBlankCheck(List<String> imgUrls) {
        if(imgUrls == null || imgUrls.isEmpty()){
            throw new RecordException(RecordErrorCode.WRONG_INPUT_IMAGE);
        }
    }

    private Member findCurrentMember() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        Member member = memberRepository.findByUsername(authentication.getName()).get();
        return member;
    }
}
