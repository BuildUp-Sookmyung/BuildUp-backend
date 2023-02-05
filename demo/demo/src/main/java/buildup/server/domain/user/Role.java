package buildup.server.domain.user;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

import java.util.Arrays;

@Getter
@RequiredArgsConstructor
public enum Role {

    //스프링 시큐리티 요구사항 : 권한 코드 앞에 ROLE_이 있어야 함
    //따라서 코드 별 키값 지정해줌
    GUEST("ROLE_GUEST", "손님"),
    USER("ROLE_USER", "일반 사용자");

    private final String key;
    private final String title;

    public static Role of(String key) {
        return Arrays.stream(Role.values())
                .filter(r -> r.getKey().equals(key))
                .findAny()
                .orElse(GUEST);
    }

}
