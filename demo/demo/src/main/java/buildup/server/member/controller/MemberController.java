package buildup.server.member.controller;

import buildup.server.auth.domain.AuthInfo;
import buildup.server.auth.dto.CodeDto;
import buildup.server.auth.dto.TokenDto;
import buildup.server.auth.dto.TokenRequestDto;
import buildup.server.auth.service.AuthService;
import buildup.server.common.response.IdResponse;
import buildup.server.common.response.StatusResponse;
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
import org.springframework.http.HttpStatus;
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
    public StringResponse sendMail(@Valid @RequestBody EmailAuthRequest emailDto) throws MessagingException {
        String name = emailDto.getName();
        String email = emailDto.getEmail();
        String str = emailService.sendEmail(name, email);

        return new StringResponse("인증코드 메일을 전송했습니다. 인증코드: " + str);
    }


    @PostMapping("/find-id")
    public IdResponse findIdAndDate(@Valid @RequestBody EmailAuthRequest codeDto) {

        String[] result = emailService.findIdAndDate(codeDto.getEmail());
        String username = result[0];
        String createdAt = result[1];

        if(username != null){
            return new IdResponse(username, createdAt);
        }
        throw new MemberException(MemberErrorCode.MEMBER_NOT_FOUND);

    }

    @PostMapping("/find-pw")
    public StringResponse findPw(@Valid @RequestBody NewLoginRequest dto) {
        emailService.updatePw(dto);
//        Authentication authentication = authenticationManager.authenticate(
//                new UsernamePasswordAuthenticationToken(dto.getEmail(), dto.getPassword()));
//        SecurityContextHolder.getContext().setAuthentication(authentication);
        return new StringResponse("비밀번호 재설정이 완료되었습니다.");

    }

    @PostMapping("/local")
    public TokenDto joinByLocalAccount(@Valid @RequestBody LocalJoinRequest request) throws IOException {
        AuthInfo info = memberService.join(request);
        return new TokenDto(info.getAccessToken().getToken(), info.getMemberRefreshToken().getRefreshToken());
    }

    @PostMapping("/id-check")
    public StringResponse checkUsername(@Valid @RequestBody IdDto id) {
        memberService.verifyDuplicatedId(id.getId());
        return new StringResponse("사용 가능한 아이디 입니다.");
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
    public TokenDto joinBySocialAccount(@Valid @RequestBody SocialJoinRequest request ) throws IOException {
        Provider.toProvider(request.getProvider());
        AuthInfo info = memberService.join(request);
        return new TokenDto(info.getAccessToken().getToken(), info.getMemberRefreshToken().getRefreshToken());
    }

    @PostMapping("/reissue")
    public TokenDto reissueToken(@Valid @RequestBody TokenDto tokenDto) {
        AuthInfo info = authService.reissueToken(tokenDto);
        return new TokenDto(info.getAccessToken().getToken(), info.getMemberRefreshToken().getRefreshToken());
    }


}
