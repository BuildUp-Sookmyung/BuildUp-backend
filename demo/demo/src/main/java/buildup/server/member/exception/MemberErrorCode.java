package buildup.server.member.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public enum MemberErrorCode {

    MEMBER_NOT_FOUND("회원을 찾을 수 없습니다."),
    MEMBER_CODE_NOT_FOUND("인증코드를 찾을 수 없습니다."),
    MEMBER_DUPLICATED("이미 가입된 회원입니다."),
    MEMBER_EMAIL_AUTH_FAILED("이메일 인증에 실패하였습니다."),

    MEMBER_PW_UPDATE_FAILED("비밀번호 재설정에 실패하였습니다."), 

    MEMBER_NOT_AUTHENTICATED("인증되지 않은 사용자입니다. 이메일 인증 먼저 진행해주세요.");


    private String defaultMessage;
}
