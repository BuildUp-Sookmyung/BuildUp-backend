package buildup.server.common.exception;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public enum S3ErrorCode {

    IMAGE_UPLOAD_FAILED("이미지 업로드에 실패했습니다.");

    private String defaultMessage;
}
