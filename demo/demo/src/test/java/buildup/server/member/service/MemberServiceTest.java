package buildup.server.member.service;

import buildup.server.entity.Interest;
import buildup.server.member.dto.LocalJoinRequest;
import buildup.server.member.dto.ProfileSaveRequest;
import buildup.server.member.repository.MemberRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentMatchers;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class MemberServiceTest {

    @InjectMocks
    private MemberService memberService;

    @Mock
    private MemberRepository memberRepository;

    @Test
    public void 일반회원가입_test() throws Exception {
        // given
        ArrayList<String> interests = new ArrayList<>();
        interests.add("연구/개발");
        interests.add("디자인");
        LocalJoinRequest request = new LocalJoinRequest(
                "username",
                "password4321",
                new ProfileSaveRequest("jojo",
                        "username@naver.com",
                        "Sookmyung Women's Universitiy",
                        "Computer Science", "4", "N", interests),
                "Y"
        );

        Mockito.when(memberRepository.findByUsername(ArgumentMatchers.any())).thenReturn(Optional.empty());

        // when

        // then

    }
}