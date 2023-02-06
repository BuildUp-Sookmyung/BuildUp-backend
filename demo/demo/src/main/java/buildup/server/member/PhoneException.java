package buildup.server.member;

import lombok.Getter;

@Getter
public class PhoneException extends RuntimeException{
    private PhoneErrorCode errorCode;
    private String errorMessage;

    public PhoneException(PhoneErrorCode errorCode) {
        this.errorCode = errorCode;
        this.errorMessage = errorCode.getDefaultMessage();
    }

    public PhoneException(PhoneErrorCode errorCode, String errorMessage) {
        this.errorCode = errorCode;
        this.errorMessage = errorMessage;
    }
}
