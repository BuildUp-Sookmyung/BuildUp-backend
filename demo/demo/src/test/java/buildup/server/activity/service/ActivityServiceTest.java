package buildup.server.activity.service;

import buildup.server.activity.domain.Activity;
import buildup.server.activity.dto.ActivitySaveRequest;
import buildup.server.activity.repository.ActivityRepository;
import buildup.server.category.Category;
import buildup.server.category.CategoryRepository;
import buildup.server.common.DummyObject;
import buildup.server.member.domain.Member;
import buildup.server.member.domain.Profile;
import buildup.server.member.dto.LocalJoinRequest;
import buildup.server.member.dto.ProfileSaveRequest;
import buildup.server.member.service.MemberService;
import buildup.server.member.service.S3Service;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class ActivityServiceTest extends DummyObject {

    @InjectMocks
    private ActivityService activityService;
    @Mock
    private CategoryRepository categoryRepository;
    @Mock
    private ActivityRepository activityRepository;
    @Mock
    private MemberService memberService;
    @Mock
    private S3Service s3Service;
    @Spy
    private BCryptPasswordEncoder passwordEncoder;

    @Test
    public void 활동생성_test() throws IOException {

        ArrayList<String> interests = new ArrayList<>();
        interests.add("연구/개발");
        interests.add("디자인");
        LocalJoinRequest request = new LocalJoinRequest(
                "username",
                passwordEncoder.encode("password4321"),
                new ProfileSaveRequest("jojo",
                        "username@naver.com",
                        "Sookmyung Women's Universitiy",
                        "Computer Science", "4", "N", interests),
                "Y"
        );
        Member member = newMember(request);
        Profile profileEntity = request.getProfile().toProfile();
        profileEntity.setMember(member);

        Category category = new Category("스터디/동아리", 1L, member);
        ActivitySaveRequest dto = ActivitySaveRequest
                .builder()
                .activityName("JPA 스터디")
                .categoryId(1L)
                .roleName("member")
                .startDate(LocalDate.of(2023,8,1))
                .endDate(LocalDate.of(2023,8,31))
                .build();

        Mockito.when(memberService.findCurrentMember()).thenReturn(member);
        Mockito.when(categoryRepository.findById(ArgumentMatchers.anyLong())).thenReturn(Optional.of(category));
        Mockito.when(activityRepository.save(ArgumentMatchers.any())).thenReturn(dto.toActivity());
        Mockito.when(s3Service.uploadActivity(ArgumentMatchers.anyLong(), ArgumentMatchers.any())).thenReturn("sample-url");

        Long activityId = activityService.createActivity(dto, new MockMultipartFile(
                "image", "profile.png", "image/png", new FileInputStream("")));

        Assertions.assertThat(activityId).isEqualTo(1L);

    }

}