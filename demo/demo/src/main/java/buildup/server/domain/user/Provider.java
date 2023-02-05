package buildup.server.domain.user;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum Provider {
    LOCAL("local"), GOOGLE("google"), KAKAO("kakao"), NAVER("naver");

    private String title;
}
