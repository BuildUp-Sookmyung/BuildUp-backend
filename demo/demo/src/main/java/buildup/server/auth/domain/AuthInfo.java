package buildup.server.auth.domain;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class AuthInfo {

    private AuthToken accessToken;
    private MemberRefreshToken memberRefreshToken;
}
