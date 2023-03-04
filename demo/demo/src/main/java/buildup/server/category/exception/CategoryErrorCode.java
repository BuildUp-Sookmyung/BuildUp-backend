package buildup.server.category.exception;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public enum CategoryErrorCode {

    CATEGORY_NOT_FOUND("해당 카테고리를 찾을 수 없습니다."),
    CATEGORY_DUPLICATED("이미 같은 이름으로 저장된 카테고리가 존재합니다."),
    CATEGORY_NO_AUTH("해당 카테고리의 접근 권한이 없습니다.");

    private String defaultErrorMessage;
}
