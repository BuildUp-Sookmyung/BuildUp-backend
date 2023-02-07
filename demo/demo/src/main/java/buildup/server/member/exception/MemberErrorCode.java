package buildup.server.member.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public enum MemberErrorCode {

    MEMBER_NOT_FOUND("회원을 찾을 수 없습니다."),
    MEMBER_DUPLICATED("이미 가입된 회원입니다.");

    private String defaultMessage;
}
