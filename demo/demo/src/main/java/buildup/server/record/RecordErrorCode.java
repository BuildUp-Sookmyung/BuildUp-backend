package buildup.server.record;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public enum RecordErrorCode {

    FILE_COUNT_EXCEED("파일 개수가 초과되었습니다 (3개 초과)"),

    WRONG_INPUT_IMAGE("잘못된 img 경로입니다."),

    WRONG_IMAGE_FORMAT("잘못된 img 형식입니다."),

    IMAGE_UPLOAD_ERROR("이미지 업로드 에러");
    private String defaultMessage;
}
