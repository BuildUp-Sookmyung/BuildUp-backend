package buildup.server.activity.exception;

import buildup.server.category.exception.CategoryException;
import buildup.server.common.response.ErrorEntity;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@Slf4j
@RestControllerAdvice
public class ActivityExceptionAdvice {

    @ExceptionHandler(ActivityException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorEntity activityException(ActivityException ex) {
        log.error("Activity Exception[{}]: {}", ex.getErrorCode().toString(), ex.getErrorMessage());
        return new ErrorEntity(ex.getErrorCode().toString(), ex.getErrorMessage());
    }
}
