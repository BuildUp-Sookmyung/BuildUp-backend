package buildup.server.auth.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Entity
public class MemberRefreshToken {

    @JsonIgnore
    @Id
    @Column(name = "refresh_token_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long refreshTokenId;

    @Column(name="member_id", unique = true)
    @NotNull
    private String username;

    @Column(name="refresh_token")
    @NotNull
    private String refreshToken;

    public MemberRefreshToken(String username, String refreshToken) {
        this.username = username;
        this.refreshToken = refreshToken;
    }
}
