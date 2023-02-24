package buildup.server.category.exception;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class CategoryException extends RuntimeException{

    private CategoryErrorCode errorCode;
    private String errorMessage;

    public CategoryException(CategoryErrorCode errorCode) {
        super(errorCode.getDefaultErrorMessage());
        this.errorCode = errorCode;
        this.errorMessage = errorCode.getDefaultErrorMessage();
    }

    public CategoryException(CategoryErrorCode errorCode, String errorMessage) {
        super(errorMessage);
        this.errorCode = errorCode;
        this.errorMessage = errorMessage;
    }
}
