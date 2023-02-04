package buildup.server.dto;
import lombok.Data;

@Data
public class LocalLoginRequestDto {
    private final String username;
    private final String password;
}
