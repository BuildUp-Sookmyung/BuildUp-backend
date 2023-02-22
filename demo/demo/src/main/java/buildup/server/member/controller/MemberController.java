package buildup.server.member.controller;

import buildup.server.auth.domain.AuthInfo;
import buildup.server.auth.dto.EmailDto;
import buildup.server.auth.dto.TokenDto;
import buildup.server.auth.service.AuthService;
import buildup.server.common.response.IdResponse;
import buildup.server.common.response.StringResponse;
import buildup.server.member.domain.Provider;
import buildup.server.member.dto.*;
import buildup.server.member.exception.MemberErrorCode;
import buildup.server.member.exception.MemberException;
import buildup.server.member.service.EmailService;
import buildup.server.member.service.MemberService;
import jakarta.mail.MessagingException;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/member")
public class MemberController {
    private final MemberService memberService;
    private final AuthService authService;
    private final EmailService emailService;

    private final AuthenticationManager authenticationManager;

    @PostMapping("/email")
    public StringResponse sendMail(@RequestBody EmailAuthRequest emailDto) throws MessagingException {
        String name = emailDto.getName();
        String email = emailDto.getEmail();
        String str = emailService.sendEmail(name, email);

        return new StringResponse("인증코드 메일을 전송했습니다. 인증코드: " + str);
    }

    @PostMapping("/code")
    public StringResponse verifyCode(@RequestBody EmailCodeRequest codeDto) {
        Long codeId = emailService.verifyCodeByRdb(codeDto.getEmail(), codeDto.getInput());
        if (codeId != null) {
            log.info("이메일 인증 성공");
            return new StringResponse("인증에 성공하였습니다. id = " + codeId);
        }
        throw new MemberException(MemberErrorCode.MEMBER_EMAIL_AUTH_FAILED);
    }


    @PostMapping("/find-id")
    public IdResponse findIdAndDate(@RequestBody EmailAuthRequest codeDto) {
        String[] result = emailService.findIDandDate(codeDto.getEmail());
        String username = result[0];
        String createdAt = result[1];

        if(username != null){
            return new IdResponse(username, createdAt);
        }
        throw new MemberException(MemberErrorCode.MEMBER_NOT_FOUND);

    }

    @PostMapping("/find-pw")
    public StringResponse findPw(@RequestBody NewLoginRequest dto) {
        emailService.updatePW(dto);

//        Authentication authentication = authenticationManager.authenticate(
//                new UsernamePasswordAuthenticationToken(dto.getEmail(), dto.getPassword()));
//        SecurityContextHolder.getContext().setAuthentication(authentication);

        return new StringResponse("비밀번호 재설정이 완료되었습니다.");
//        if (emailService.UpdatePW(newloginDto.getPassword())) {
//            log.info("비밀번호 재설정 성공");
//            return new StringResponse("비밀번호 재설정이 완료되었습니다.");
//        }
//        throw new MemberException(MemberErrorCode.MEMBER_EMAIL_AUTH_FAILED);


    }


    //TODO: 시간 체크 받을 엔드포인트
    @PostMapping("/time")
    public StringResponse deleteCode(@RequestBody EmailDto email) {
        if (emailService.deleteCode(email)) {
            return new StringResponse("만료처리");
        }
        throw new MemberException(MemberErrorCode.MEMBER_NOT_FOUND);
    }


    @PostMapping("/local")
    public TokenDto joinByLocalAccount(@Valid @RequestPart LocalJoinRequest request,
                                       @RequestPart MultipartFile img) throws IOException {
        AuthInfo info = memberService.join(request, img);
        return new TokenDto(info.getAccessToken().getToken(), info.getMemberRefreshToken().getRefreshToken());
    }

    @PostMapping("/login")
    public TokenDto signInByLocalAccount(@Valid @RequestBody LoginRequest loginRequest) {
        AuthInfo info = memberService.signIn(loginRequest);
        return new TokenDto(info.getAccessToken().getToken(), info.getMemberRefreshToken().getRefreshToken());
    }

    @PostMapping("/social-access")
    public StringResponse accessBySocialAccount(@Valid @RequestBody SocialLoginRequest request) {
        Provider.toProvider(request.getProvider());
        if (memberService.verifyMember(request))
            return new StringResponse("이미 가입된 회원입니다. 로그인을 위해 토큰 요청 필요합니다.");
        return new StringResponse("신규 회원입니다. 프로필 입력 진행해주세요.");
    }

    // 소셜로그인 접근 시 이미 가입된 회원일 때 토큰 반환
    @PostMapping("/social-token")
    public TokenDto signInBySocialAccount(@Valid @RequestBody SocialLoginRequest request) {
        Provider.toProvider(request.getProvider());
        AuthInfo info = memberService.signIn(request);
        return new TokenDto(info.getAccessToken().getToken(), info.getMemberRefreshToken().getRefreshToken());
    }

    // 소셜로그인 접근 시 신규 회원일 때 프로필 입력 후 토큰 반환
    @PostMapping("/social-profile")
    public TokenDto joinBySocialAccount(@Valid @RequestPart SocialJoinRequest request,
                                        @RequestPart MultipartFile img) throws IOException {
        Provider.toProvider(request.getProvider());
        AuthInfo info = memberService.join(request, img);
        return new TokenDto(info.getAccessToken().getToken(), info.getMemberRefreshToken().getRefreshToken());
    }

    @PostMapping("/reissue")
    public TokenDto reissueToken(@Valid @RequestBody TokenDto tokenDto) {
        AuthInfo info = authService.reissueToken(tokenDto);
        return new TokenDto(info.getAccessToken().getToken(), info.getMemberRefreshToken().getRefreshToken());
    }

}
