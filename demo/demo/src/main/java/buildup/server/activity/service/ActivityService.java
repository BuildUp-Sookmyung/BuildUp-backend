package buildup.server.activity.service;

import buildup.server.activity.domain.Activity;
import buildup.server.activity.dto.*;
import buildup.server.activity.exception.ActivityErrorCode;
import buildup.server.activity.exception.ActivityException;
import buildup.server.activity.repository.ActivityRepository;
import buildup.server.category.Category;
import buildup.server.category.CategoryRepository;
import buildup.server.category.CategoryService;
import buildup.server.category.exception.CategoryErrorCode;
import buildup.server.category.exception.CategoryException;
import buildup.server.member.domain.Member;
import buildup.server.member.exception.MemberErrorCode;
import buildup.server.member.exception.MemberException;
import buildup.server.member.repository.MemberRepository;
import buildup.server.member.service.MemberService;
import buildup.server.member.service.S3Service;
import buildup.server.record.domain.Record;
import buildup.server.record.exception.RecordErrorCode;
import buildup.server.record.exception.RecordException;
import buildup.server.record.repository.RecordRepository;
import buildup.server.record.service.RecordService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.time.Duration;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;


@Slf4j
@Service
@RequiredArgsConstructor
public class ActivityService {

    private final ActivityRepository activityRepository;
    private final MemberRepository memberRepository;
    private final MemberService memberService;
    private final CategoryRepository categoryRepository;
    private final CategoryService categoryService;
    private final RecordRepository recordRepository;
    private final S3Service s3Service;
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate nowDate;

    @Transactional
    public Long createActivity(ActivitySaveRequest requestDto, MultipartFile img) {
        Member member = memberService.findCurrentMember();

        Category category = categoryRepository.findById(requestDto.getCategoryId())
                .orElseThrow(() -> new CategoryException(CategoryErrorCode.CATEGORY_NOT_FOUND));
        categoryService.checkCategoryAuthForRead(member, category);

        Activity activity = requestDto.toActivity(member, category);
        activityRepository.save(activity);


        String activityUrl = null;
        if (! img.isEmpty())
            activityUrl = s3Service.uploadActivity(activity.getId(), img);
        activity.setActivityImg(activityUrl);
        return activity.getId();
    }

    // 기록(메인) - 전체
    @Transactional(readOnly = true)
    public List<ActivityListResponse> readMyActivities() {
        Member me = memberService.findCurrentMember();
        return readActivitiesByMember(me);
    }

    // 활동 상세
    @Transactional(readOnly = true)
    public ActivityResponse readOneActivity(Long activityId) {
        Activity activity = activityRepository.findById(activityId)
                .orElseThrow(() -> new ActivityException(ActivityErrorCode.ACTIVITY_NOT_FOUND));
        return toActivityResponse(activity);
    }

    // 기록(메인) - 카테고리별
    @Transactional(readOnly = true)
    public List<ActivityListResponse> readMyActivitiesByCategory(Long categoryId) {
        Member me = memberService.findCurrentMember();
        Category category = categoryRepository.findById(categoryId)
                .orElseThrow(() -> new CategoryException(CategoryErrorCode.CATEGORY_NOT_FOUND));
        categoryService.checkCategoryAuthForRead(me, category);
        return readActivitiesByMemberAndCategory(me, category);
    }

    // 홈 - 기록 필터링
    @Transactional(readOnly = true)
    public List<SearchResult> readActivitiesByFilter(FilterVO filter) {
        Member me = memberService.findCurrentMember();
        List<Activity> activities = activityRepository.findAllByMember(me);

        if (!filter.getStart().isEmpty() && !filter.getEnd().isEmpty()) {
            LocalDate startDate = convertLocalDate(filter.getStart());
            LocalDate end = convertLocalDate(filter.getEnd());
            LocalDate endDate = end.withDayOfMonth(end.lengthOfMonth());

            if (startDate.isAfter(endDate))
                throw new ActivityException(ActivityErrorCode.ACTIVITY_DATE_ERROR);

            activities = activities.stream().filter(
                    a -> a.getStartDate().isAfter(startDate) && a.getEndDate().isBefore(endDate)
                    ).collect(Collectors.toList());
        }

        List<String> categories = filter.getCategories();
        if (categories.isEmpty()) {
            categories = List.of("대외활동", "공모전", "동아리", "프로젝트", "자격증", "교내활동");
        }

        List<SearchResult> results = new ArrayList<>();
        for (String categoryName : categories) {
            Category category = categoryRepository.findByName(categoryName)
                    .orElseThrow(() -> new CategoryException(CategoryErrorCode.CATEGORY_NOT_FOUND));
            List<Activity> activityList = activities.stream().filter(
                    a -> category.equals(a.getCategory())
            ).collect(Collectors.toList());
            results.add(new SearchResult(categoryName, toDtoList(activityList)));
        }

        return results;
    }

    // 프로필 검색 상세(약력)
    @Transactional(readOnly = true)
    public List<SearchResult> readActivitiesByProfile(Long profileId) {
        Member member = memberRepository.findById(profileId)
                .orElseThrow(() -> new MemberException(MemberErrorCode.MEMBER_NOT_FOUND));
        List<SearchResult> results = new ArrayList<>();
        for (Long id=1L; id<7L; id++) {
            Category category = categoryRepository.findById(id).get();
            results.add(new SearchResult(category.getName(), readActivitiesByMemberAndCategory(member, category)));
        }
        return results;
    }

    @Transactional
    public void updateActivities(ActivityUpdateRequest requestDto) {
        Activity activity = activityRepository.findById(requestDto.getId())
                .orElseThrow(() -> new ActivityException(ActivityErrorCode.ACTIVITY_NOT_FOUND));
        Category category = categoryRepository.findById(requestDto.getCategoryId())
                .orElseThrow(() -> new CategoryException(CategoryErrorCode.CATEGORY_NOT_FOUND));
        activity.updateActivity(category, requestDto.getActivityName(), requestDto.getHostName(), requestDto.getRoleName(),
                requestDto.getStartDate(), requestDto.getEndDate(),requestDto.getUrlName());
    }

    @Transactional
    public void updateActivityImages(ActivityImageUpdateRequest requestDto, MultipartFile img) {
        Member member = memberService.findCurrentMember();
        Activity activity = activityRepository.findById(requestDto.getActivityId())
                .orElseThrow(() -> new ActivityException(ActivityErrorCode.ACTIVITY_NOT_FOUND));

        String activity_url = activity.getActivityImg();

        if (! img.isEmpty()) {
            if (activity_url != null)
                s3Service.deleteActivity(activity_url);
            String url = s3Service.uploadActivity(activity.getId(), img);
            activity.setActivityImg(url);
        } else {
            if (activity_url != null) {
                s3Service.deleteActivity(activity_url);
                activity.setActivityImg(null);
            }
        }
    }
    @Transactional
    public void deleteActivity(Long id) {
        Activity activity= activityRepository.findById(id)
                .orElseThrow(() -> new ActivityException(ActivityErrorCode.ACTIVITY_NOT_FOUND));
        checkActivityAuth(activity, memberService.findCurrentMember());
        List<Record> childRecords = recordRepository.findAllByActivity(activity);
        recordRepository.deleteAll(childRecords);
        activityRepository.delete(activity);
    }
    private LocalDate convertLocalDate(String value) {
        return LocalDate.of(Integer.valueOf(value.substring(0,4)),
                Integer.valueOf(value.substring(5)),
                1);
    }

    private Integer calculatePercentage(LocalDate startDate, LocalDate endDate){

        nowDate = LocalDate.now(); //현재시간

        Duration duration = Duration.between(startDate.atStartOfDay(), endDate.atStartOfDay());
        double betweenDays = (double) duration.toDays(); //간격(일기준)

        Duration duration1 = Duration.between(startDate.atStartOfDay(), nowDate.atStartOfDay());
        double startAndNow = (double) duration1.toDays();

        Integer percentage = (int) (((startAndNow + 1) / (betweenDays + 1)) * 100);

        if (percentage >= 100){ percentage = 100; }
        else if (percentage <= 0) { percentage = 0; }

        return percentage;
    }

    private void checkActivityAuth(Activity activity, Member member) {
        if (! activity.getMember().equals(member))
            throw new ActivityException(ActivityErrorCode.ACTIVITY_NO_AUTH);
    }

    private List<ActivityListResponse> readActivitiesByMember(Member member) {
        return toDtoList(activityRepository.findAllByMember(member));
    }

    private List<ActivityListResponse> readActivitiesByMemberAndCategory(Member member, Category category) {
        return toDtoList(activityRepository.findAllByMemberAndCategory(member, category));
    }

    private List<ActivityListResponse> toDtoList(List<Activity> entities) {
        List<ActivityListResponse> dtoList = new ArrayList<>();

        for (Activity entity : entities)
            dtoList.add(new ActivityListResponse(
                    entity.getId(),
                    entity.getName(),
                    entity.getCategory().getName(),
                    entity.getStartDate(),
                    entity.getEndDate(),
                    calculatePercentage(entity.getStartDate(), entity.getEndDate())
                    )
            );

        return dtoList;
    }

    private ActivityResponse toActivityResponse(Activity activity) {
        return new ActivityResponse(activity.getId(), activity.getCategory().getName(), activity.getName(),
                activity.getHost(), activity.getActivityImg(), activity.getRole(), activity.getStartDate(), activity.getEndDate(),
                activity.getUrl());
    }


}
