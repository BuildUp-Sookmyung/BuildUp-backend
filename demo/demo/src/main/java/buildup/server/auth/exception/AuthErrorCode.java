package buildup.server.auth.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public enum AuthErrorCode {

    AUTH_PROVIDER_MISS_MATCH("소셜 로그인 provider 정보가 일치하지 않습니다."),
    UNAUTHORIZED("인증되지 않은 사용자입니다."),
    INVALID_ACCESS_TOKEN("유효하지 않은 토큰입니다."),
    INVALID_REFRESH_TOKEN("리프레시 토큰이 유효하지 않습니다."),
    NOT_EXPIRED_TOKEN_YET("아직 토큰이 만료되지 않았습니다."),
    INVALID_TOKEN_SIGNATURE("토큰의 서명이 유효하지 않습니다."),
    EXPIRED_JWT_TOKEN("토큰이 만료되었습니다."),
    UNSUPPORTED_JWT_TOKEN("지원하지 않는 토큰 형식입니다."),
    CREDENTIAL_MISS_MATCH("비밀번호가 틀렸습니다.");

    private String defaultMessage;
}
