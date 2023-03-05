package buildup.server.auth.repository;

import buildup.server.auth.domain.MemberRefreshToken;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface RefreshTokenRepository extends JpaRepository<MemberRefreshToken, Long> {

    MemberRefreshToken findByUsername(String username);
    MemberRefreshToken findByUsernameAndRefreshToken(String username, String refreshToken);
}
