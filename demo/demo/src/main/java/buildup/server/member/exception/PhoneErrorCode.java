package buildup.server.member.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public enum PhoneErrorCode {

    PHONE_DUPLICATED("이미 가입된 전화번호가 존재합니다.");

    private String defaultMessage;
}
