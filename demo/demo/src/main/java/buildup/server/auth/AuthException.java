package buildup.server.auth;

import buildup.server.member.PhoneErrorCode;
import lombok.Getter;

@Getter
public class AuthException extends RuntimeException{
    private AuthErrorCode errorCode;
    private String errorMessage;

    public AuthException(AuthErrorCode errorCode) {
        this.errorCode = errorCode;
        this.errorMessage = errorCode.getDefaultMessage();
    }

    public AuthException(AuthErrorCode errorCode, String errorMessage) {
        this.errorCode = errorCode;
        this.errorMessage = errorMessage;
    }
}
