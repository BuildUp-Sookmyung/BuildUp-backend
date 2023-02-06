package buildup.server.member;

import lombok.Getter;

@Getter
public class MemberException extends RuntimeException{
    private MemberErrorCode errorCode;
    private String errorMessage;

    public MemberException(MemberErrorCode errorCode) {
        this.errorCode = errorCode;
        this.errorMessage = errorCode.getDefaultMessage();
    }

    public MemberException(MemberErrorCode errorCode, String errorMessage) {
        this.errorCode = errorCode;
        this.errorMessage = errorMessage;
    }
}
