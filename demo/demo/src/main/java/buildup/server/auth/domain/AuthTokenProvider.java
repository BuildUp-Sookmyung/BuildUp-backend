package buildup.server.auth.domain;

import buildup.server.auth.exception.AuthErrorCode;
import buildup.server.auth.exception.AuthException;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.security.Keys;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;

import java.security.Key;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
public class AuthTokenProvider {

    private final Key key;
    private static final String AUTHORITIES_KEY = "role";

    public AuthTokenProvider(String secret) {
        this.key = Keys.hmacShaKeyFor(secret.getBytes());
    }

    public AuthToken createAuthToken(String id, Date expiry) {
        return new AuthToken(id, expiry, key);
    }

    public AuthToken createAuthToken(String id, String role, Date expiry) {
        return new AuthToken(id, role, expiry, key);
    }

    public AuthToken convertAuthToken(String token) {
        return new AuthToken(token, key);
    }

    public Authentication getAuthentication(AuthToken authToken) {

       if (authToken.validate()) {
           Claims claims = authToken.getTokenClaims();
           List<? extends GrantedAuthority> authorities = Arrays.stream(new String[]{claims.get(AUTHORITIES_KEY).toString()})
                   .map(SimpleGrantedAuthority::new)
                   .collect(Collectors.toList());

           User principal = new User(claims.getSubject(),
                   PasswordEncoderFactories.createDelegatingPasswordEncoder().encode("pw1234"),
                   authorities);
           return new UsernamePasswordAuthenticationToken(principal, authToken, authorities);
       }
       throw new AuthException(AuthErrorCode.UNAUTHORIZED);
    }
}
