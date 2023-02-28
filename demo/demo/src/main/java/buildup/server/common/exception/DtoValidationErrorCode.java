package buildup.server.common.exception;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public enum DtoValidationErrorCode {

    BAD_INPUT("입력이 올바르지 않습니다.");

    private String defaultMessage;
}
