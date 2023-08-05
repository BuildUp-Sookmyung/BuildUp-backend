package buildup.server.member.service;

import buildup.server.common.DummyObject;
import buildup.server.entity.Interest;
import buildup.server.member.domain.Member;
import buildup.server.member.domain.Profile;
import buildup.server.member.dto.LocalJoinRequest;
import buildup.server.member.dto.ProfileSaveRequest;
import buildup.server.member.repository.InterestRepository;
import buildup.server.member.repository.ProfileRepository;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class ProfileServiceTest extends DummyObject {

    @InjectMocks
    private ProfileService profileService;
    @Mock
    private ProfileRepository profileRepository;
    @Mock
    private InterestRepository interestRepository;
    @Spy
    private BCryptPasswordEncoder passwordEncoder;

    @Test
    public void 프로필생성_test() throws Exception {
        // given
        List<String> interests = new ArrayList<>();
        interests.add("연구/개발");
        interests.add("디자인");
        ProfileSaveRequest profileRequest = new ProfileSaveRequest("nickname", "username@naver.com",
                "Sookmyung Women's University",
                "Computer Science", "4", "N", interests);
        LocalJoinRequest loginRequest = new LocalJoinRequest(
                "username",
                "password4321",
                profileRequest,
                "Y"
        );

        Member member = newMember(loginRequest);
        Profile profile = profileRequest.toProfile();
        List<Interest> interestList = new ArrayList<>();
        Interest interest1 = new Interest(profile, "연구/개발");
        Interest interest2 = new Interest(profile, "디자인");
        interestList.add(interest1);
        interestList.add(interest2);

        // when
        Mockito.when(interestRepository.save(ArgumentMatchers.any())).thenReturn(interest1);
        Mockito.when(interestRepository.save(ArgumentMatchers.any())).thenReturn(interest2);
        Mockito.when(profileRepository.save(ArgumentMatchers.any())).thenReturn(profile);
        Long profileId = profileService.saveProfile(profileRequest, member);
        // then
        Assertions.assertThat(profileId).isEqualTo(member.getId());
        Assertions.assertThat(profile.getEmail()).isEqualTo("username@naver.com");

    }

}