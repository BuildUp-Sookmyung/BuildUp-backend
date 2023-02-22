package buildup.server.member.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class LoginRequest {
    private String username;
    private String password;

    public static LoginRequest toLoginRequest(LocalJoinRequest request) {
        return new LoginRequest(request.getUsername(), request.getPassword());
    }

    public static LoginRequest toLoginRequest(SocialLoginRequest request, String pw) {
        return new LoginRequest(request.getProvider()+request.getEmail(), pw);
    }

    public static LoginRequest toLoginRequest(SocialJoinRequest request, String pw) {
        return new LoginRequest(request.getProvider()+request.getProfile().getEmail(), pw);
    }
}
