package buildup.server.common.exception;

import lombok.Getter;

@Getter
public class S3Exception extends RuntimeException{

    private S3ErrorCode errorCode;
    private String errorMessage;

    public S3Exception(S3ErrorCode errorCode) {
        super(errorCode.getDefaultMessage());
        this.errorCode = errorCode;
        this.errorMessage = errorCode.getDefaultMessage();
    }

    public S3Exception(S3ErrorCode errorCode, String errorMessage) {
        super(errorMessage);
        this.errorCode = errorCode;
        this.errorMessage = errorMessage;
    }
}
