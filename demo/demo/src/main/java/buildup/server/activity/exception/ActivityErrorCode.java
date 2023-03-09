package buildup.server.activity.exception;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public enum ActivityErrorCode {

    ACTIVITY_NOT_FOUND("선택 활동을 찾을 수 없습니다."),
    ACTIVITY_DATE_ERROR("종료일은 시작일 이후여야 합니다."),
    ACTIVITY_NO_AUTH("선택 활동의 접근 권한이 없습니다.");

    private String defaultErrorMessage;
}
