package buildup.server.auth.exception;

import lombok.Getter;

@Getter
public class AuthException extends RuntimeException{
    private AuthErrorCode errorCode;
    private String errorMessage;

    public AuthException(AuthErrorCode errorCode) {
        super(errorCode.getDefaultMessage());
        this.errorCode = errorCode;
        this.errorMessage = errorCode.getDefaultMessage();
    }

    public AuthException(AuthErrorCode errorCode, String errorMessage) {
        super(errorCode.getDefaultMessage());
        this.errorCode = errorCode;
        this.errorMessage = errorMessage;
    }
}
