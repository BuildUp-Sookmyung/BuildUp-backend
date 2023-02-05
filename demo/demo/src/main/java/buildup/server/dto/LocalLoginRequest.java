package buildup.server.dto;
import lombok.Data;

@Data
public record LocalLoginRequest(String username, String password) {}
