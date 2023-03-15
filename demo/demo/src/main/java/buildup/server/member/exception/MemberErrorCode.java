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
    MEMBER_PROFILE_BAD_REQUEST("img가 null일 수 없습니다."),

    ACCOUNT_IN_SOCIAL("일반 회원가입으로 가입한 계정이 아닙니다."),
    MEMBER_PW_UPDATE_FAILED("비밀번호 재설정에 실패하였습니다.");


    private String defaultMessage;
}
