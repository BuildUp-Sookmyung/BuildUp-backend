package buildup.server.member.controller;

import buildup.server.auth.domain.AuthInfo;
import buildup.server.auth.dto.TokenDto;
import buildup.server.auth.service.AuthService;
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

    @PostMapping("/email")
    public StringResponse sendMail(@RequestBody EmailAuthRequest emailDto) throws MessagingException {
        String name = emailDto.getName();
        String email = emailDto.getEmail();
        emailService.sendEmail(name, email);

        return new StringResponse("인증코드 메일을 전송했습니다.");
    }

    @PostMapping("/code")
    public StringResponse verifyCode(@RequestBody EmailCodeRequest codeDto) {
        if (emailService.verifyByCode(codeDto.getEmail(), codeDto.getInput())) {
            log.info("이메일 인증 성공");
            return new StringResponse("인증에 성공하였습니다.");
        }
        throw new MemberException(MemberErrorCode.MEMBER_EMAIL_AUTH_FAILED);
    }

    @PostMapping("/local")
    public TokenDto joinByLocalAccount(@Valid @RequestPart LocalJoinRequest localJoinRequest,
                                       @RequestPart MultipartFile img) throws IOException {
        AuthInfo info = memberService.join(localJoinRequest, img);
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
