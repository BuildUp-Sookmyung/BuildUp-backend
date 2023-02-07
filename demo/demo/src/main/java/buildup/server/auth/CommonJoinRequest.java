package buildup.server.auth;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class CommonJoinRequest {
    private String principal;
    private String credential;
}
