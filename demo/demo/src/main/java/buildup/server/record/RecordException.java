package buildup.server.record;

import buildup.server.member.exception.MemberErrorCode;
import lombok.Getter;

@Getter
public class RecordException extends RuntimeException{
    private RecordErrorCode errorCode;
    private String errorMessage;

    public RecordException(RecordErrorCode errorCode) {
        super(errorCode.getDefaultMessage());
        this.errorCode = errorCode;
        this.errorMessage = errorCode.getDefaultMessage();
    }

    public RecordException(RecordErrorCode errorCode, String errorMessage) {
        super(errorCode.getDefaultMessage());
        this.errorCode = errorCode;
        this.errorMessage = errorMessage;
    }
}
