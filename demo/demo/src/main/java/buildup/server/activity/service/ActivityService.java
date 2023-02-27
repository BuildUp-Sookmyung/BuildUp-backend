package buildup.server.activity.service;

import buildup.server.activity.domain.Activity;
import buildup.server.activity.dto.ActivityRequest;
import buildup.server.activity.exception.ActivityErrorCode;
import buildup.server.activity.exception.ActivityException;
import buildup.server.activity.repository.ActivityRepository;
import buildup.server.category.Category;
import buildup.server.category.dto.CategorySaveRequest;
import buildup.server.category.exception.CategoryErrorCode;
import buildup.server.category.exception.CategoryException;
import buildup.server.member.domain.Member;
import buildup.server.member.domain.Profile;
import buildup.server.member.dto.ProfileSaveRequest;
import buildup.server.member.service.MemberService;
import buildup.server.member.service.S3Service;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class ActivityService {

    private final ActivityRepository activityRepository;

    private final MemberService memberService;

    private final S3Service s3Service;

    @@Transactional
    public Long createActivity(ActivityRequest requestdto, Member member, Category category, MultipartFile img) {
        Activity activity = requestdto.toActivity();
        String activity_url = null;
        if (! img.isEmpty())
            activity_url = s3Service.uploadActivity(activity, member.getId(), img);
        activity.setMember(member);
        activity.setCategory(category);
        activity.setActivityimg(activity_url);
        return activityRepository.save(activity).getId();
    }


    private void checkDuplicateActivity(Member member, String activityName) {
        List<Activity> activities = activityRepository.findAllByMember(member);
        for (Activity activity : activities) {
            if (activityName.equals(activity.getName()))
                throw new ActivityException(ActivityErrorCode.ACTIVITY_DUPLICATED);
        }
    }

    private void checkActivityAuth(Member member, Activity target) {
        List<Activity> activities = activityRepository.findAllByMember(member);
        for (Activity activity : activities) {
            if (activity.equals(target))
                return;
        }
        throw new ActivityException(ActivityErrorCode.ACTIVITY_NO_AUTH);
    }
}
