package buildup.server.activity.service;

import buildup.server.activity.domain.Activity;
import buildup.server.activity.dto.ActivityListResponse;
import buildup.server.activity.dto.ActivitySaveRequest;
import buildup.server.activity.dto.ActivityResponse;
import buildup.server.activity.dto.ActivityUpdateRequest;
import buildup.server.activity.exception.ActivityErrorCode;
import buildup.server.activity.exception.ActivityException;
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

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class ActivityService {

    private final ActivityRepository activityRepository;
    private final MemberRepository memberRepository;
    private final MemberService memberService;
    private final CategoryRepository categoryRepository;
    private final CategoryService categoryService;
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
    public List<ActivityListResponse> readMyActivities() {
        Member me = memberService.findCurrentMember();
        return readActivitiesByMember(me);
    }

    @Transactional(readOnly = true)
    public ActivityResponse readOneActivity(Long activityId) {
        Activity activity = activityRepository.findById(activityId)
                .orElseThrow(() -> new ActivityException(ActivityErrorCode.ACTIVITY_NOT_FOUND));
        return ActivityResponse.toDto(activity);
    }

    @Transactional(readOnly = true)
    public List<ActivityListResponse> readMyActivitiesByCategory(Long categoryId) {
        Member me = memberService.findCurrentMember();
        Category category = categoryRepository.findById(categoryId)
                .orElseThrow(() -> new CategoryException(CategoryErrorCode.CATEGORY_NOT_FOUND));
        categoryService.checkCategoryAuthForRead(me, category);
        return readActivitiesByMemberAndCategory(me, category);
    }

    @Transactional
    public void updateActivities(ActivityUpdateRequest requestDto) {
        Activity activity = activityRepository.findById(requestDto.getId())
                .orElseThrow(() -> new ActivityException(ActivityErrorCode.ACTIVITY_NOT_FOUND));
        checkActivityAuth(activity, memberService.findCurrentMember());
        Category category = categoryRepository.findById(requestDto.getCategoryId())
                .orElseThrow(() -> new CategoryException(CategoryErrorCode.CATEGORY_NOT_FOUND));
        activity.updateActivity(category, requestDto.getActivityName(), requestDto.getHostName(), requestDto.getRoleName(),
                requestDto.getStartDate(), requestDto.getEndDate(),requestDto.getUrlName());
    }

    @Transactional
    public void updateActivityImages(MultipartFile img) {
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
    public void deleteActivity(Long id) {
        Activity activity= activityRepository.findById(id)
                .orElseThrow(() -> new ActivityException(ActivityErrorCode.ACTIVITY_NOT_FOUND));
        checkActivityAuth(activity, memberService.findCurrentMember());
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

    private void checkActivityAuth(Activity activity, Member member) {
        if (! activity.getMember().equals(member))
            throw new ActivityException(ActivityErrorCode.ACTIVITY_NO_AUTH);
    }

    private List<ActivityListResponse> readActivitiesByMember(Member member) {
        return ActivityListResponse.toDtoList(activityRepository.findAllByMember(member));
    }

    private List<ActivityListResponse> readActivitiesByMemberAndCategory(Member member, Category category) {
        return ActivityListResponse.toDtoList(activityRepository.findAllByMemberAndCategory(member, category));
    }



}
