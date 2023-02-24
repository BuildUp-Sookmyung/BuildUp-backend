package buildup.server.category.exception;

import buildup.server.common.response.ErrorEntity;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@Slf4j
@RestControllerAdvice
public class CategoryExceptionAdvice {

    @ExceptionHandler(CategoryException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorEntity categoryException(CategoryException ex) {
        log.error("Category Exception[{}]: {}", ex.getErrorCode().toString(), ex.getErrorMessage());
        return new ErrorEntity(ex.getErrorCode().toString(), ex.getErrorMessage());
    }
}
