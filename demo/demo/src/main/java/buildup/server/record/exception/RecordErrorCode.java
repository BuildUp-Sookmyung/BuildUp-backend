package buildup.server.record.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public enum RecordErrorCode {

    FILE_COUNT_EXCEED("파일 개수가 초과되었습니다 (3개 초과)"),

    NO_FILE("no file "), //test

    WRONG_INPUT_IMAGE("잘못된 img 경로입니다."),

    WRONG_IMAGE_FORMAT("잘못된 img 형식입니다."),

    IMAGE_UPLOAD_ERROR("이미지 업로드 에러"),

    WRONG_INPUT_CONTENT("잘못된 input 입니다."),

    NOT_FOUND_RECORD("기록을 찾을 수 없습니다."),

    NOT_FOUND_RECORD_IMG("기록 이미지를 찾을 수 없습니다."),

    NOT_FOUND_URL("삭제하려는 URL을 찾을 수 없습니다.");
    private String defaultMessage;
}
