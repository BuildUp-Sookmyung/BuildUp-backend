package buildup.server.dto;
import lombok.Data;

@Data
public class LocalLoginRequest {
    private final String username;
    private final String password;
}
