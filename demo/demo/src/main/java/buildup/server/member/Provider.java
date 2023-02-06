package buildup.server.member;

import com.fasterxml.jackson.annotation.JsonValue;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.Arrays;

@Getter
@AllArgsConstructor
public enum Provider {
    LOCAL("LOCAL"),
    GOOGLE("GOOGLE"),
    KAKAO("KAKAO"),
    NAVER("NAVER");

    @JsonValue
    private final String title;

    public static Provider toProvider(String str) {
        return Arrays.stream(Provider.values())
                .filter(provider -> provider.title.equals(str))
                .findAny()
                .orElseThrow(() -> new IllegalArgumentException("연산자를 찾을 수 없습니다."));
    }

}
