package buildup.server.member.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public enum MemberErrorCode {

    MEMBER_NOT_FOUND("회원을 찾을 수 없습니다."),
    MEMBER_DUPLICATED("이미 가입된 회원입니다."),
    MEMBER_EMAIL_AUTH_FAILED("이메일 인증에 실패하였습니다."),
    MEMBER_PW_UPDATE_FAILED("비밀번호 재설정에 실패하였습니다.");



    private String defaultMessage;
}
