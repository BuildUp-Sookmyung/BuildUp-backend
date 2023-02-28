package buildup.server.member.exception;

import buildup.server.common.exception.DtoValidationErrorCode;
import buildup.server.common.response.ErrorEntity;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.HashMap;
import java.util.Map;

@Slf4j
@RestControllerAdvice
public class MemberExceptionAdvice {

    @ExceptionHandler(MemberException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorEntity handleAuthException(MemberException e) {
        log.error("Member Exception({})={}", e.getErrorCode(), e.getErrorMessage());
        return new ErrorEntity(e.getErrorCode().toString(), e.getErrorMessage());
    }
}
