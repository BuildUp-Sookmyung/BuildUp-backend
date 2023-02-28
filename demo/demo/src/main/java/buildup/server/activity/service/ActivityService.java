package buildup.server.activity.service;

import buildup.server.activity.domain.Activity;
import buildup.server.activity.dto.ActivitySaveRequest;
import buildup.server.activity.dto.ActivityResponse;
import buildup.server.activity.dto.ActivityUpdateRequest;
import buildup.server.activity.exception.ActivityErrorCode;
import buildup.server.activity.exception.ActivityException;
import buildup.server.activity.repository.ActivityRepository;
import buildup.server.category.Category;
import buildup.server.category.CategoryRepository;
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

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class ActivityService {

    private final ActivityRepository activityRepository;

    private final MemberRepository memberRepository;

    private final MemberService memberService;

    private final CategoryRepository categoryRepository;

    private final S3Service s3Service;

    @Transactional
    public Long createActivity(ActivitySaveRequest requestDto, MultipartFile img) {
        Member member = memberService.findCurrentMember();
        checkDuplicateActivity(member, requestDto.getActivityName());

        Activity activity = requestDto.toActivity();
        Category category = categoryRepository.findById(requestDto.getCategoryId())
                .orElseThrow(() -> new CategoryException(CategoryErrorCode.CATEGORY_NOT_FOUND));
        activity.setCategory(category);

        String activity_url = null;
        if (! img.isEmpty())
            activity_url = s3Service.uploadActivity(activity, member.getId(), img);
        activity.setMember(member);
        activity.setActivityimg(activity_url);
        return activityRepository.save(activity).getId();
    }

    @Transactional(readOnly = true)
    public List<ActivityResponse> readMyActivities() {
        Member me = memberService.findCurrentMember();
        return readActivitiesByMember(me);
    }

    @Transactional
    public void updateActivityS(ActivityUpdateRequest requestdto) {
//        Member member = memberService.findCurrentMember();
        Activity activity = activityRepository.findById(requestdto.getId())
                .orElseThrow(() -> new ActivityException(ActivityErrorCode.ACTIVITY_NOT_FOUND));
        activity.updateActivity(requestdto.getCategoryId(), requestdto.getActivityName(), requestdto.getHostName(), requestdto.getRoleName(),
                requestdto.getStartDate(), requestdto.getEndDate(),requestdto.getUrlName());
    }
    @Transactional
    public void updateActivityImageS(MultipartFile img) {
        Member member = findCurrentMember();
        Activity activity = activityRepository.findById(member.getId()).get();

        String activity_url = activity.getActivityimg();

        if (! img.isEmpty()) {
            String url = s3Service.uploadActivity(activity, member.getId(), img);
            activity.setActivityimg(url);
        } else if (activity_url!=null) {
            s3Service.deleteProfile(activity_url);
            activity.setActivityimg(null);
        }
    }

    @Transactional
    public void deleteActivityS(Long id) {
        Activity activity= activityRepository.findById(id)
                .orElseThrow(() -> new ActivityException(ActivityErrorCode.ACTIVITY_NOT_FOUND));
        activityRepository.delete(activity);
    }

    private Member findCurrentMember() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        Member member = memberRepository.findByUsername(authentication.getName()).get();
        return member;
    }
    private void checkDuplicateActivity(Member member, String activityName) {
        List<Activity> activities = activityRepository.findAllByMember(member);
        for (Activity activity : activities) {
            if (activityName.equals(activity.getName()))
                throw new ActivityException(ActivityErrorCode.ACTIVITY_DUPLICATED);
        }
    }

    private List<ActivityResponse> readActivitiesByMember(Member member) {
        return ActivityResponse.toDtoList(activityRepository.findAllByMember(member));
    }



}
