package buildup.server.member.service;

import buildup.server.auth.domain.AuthInfo;
import buildup.server.auth.service.AuthService;
import buildup.server.common.DummyObject;
import buildup.server.entity.Interest;
import buildup.server.member.domain.Member;
import buildup.server.member.domain.Profile;
import buildup.server.member.dto.LocalJoinRequest;
import buildup.server.member.dto.ProfileSaveRequest;
import buildup.server.member.repository.MemberRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.test.context.support.WithSecurityContext;
import org.springframework.security.test.context.support.WithSecurityContextFactory;

import java.util.ArrayList;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class MemberServiceTest extends DummyObject {

    @InjectMocks
    private MemberService memberService;
    @Mock
    private MemberRepository memberRepository;
    @Mock
    private ProfileService profileService;
    @Mock
    private AuthService authService;
    @Spy
    private BCryptPasswordEncoder passwordEncoder;

    @Test
    public void 일반회원가입_test() throws Exception {
        // given
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

        Mockito.when(memberRepository.findByUsername(ArgumentMatchers.any())).thenReturn(Optional.empty());

        // when
        Mockito.when(memberRepository.save(ArgumentMatchers.any())).thenReturn(member);
        Mockito.when(profileService.saveProfile(ArgumentMatchers.any(), ArgumentMatchers.any())).thenReturn(1L);
        AuthInfo authInfo = memberService.join(request);

        // then

    }
}