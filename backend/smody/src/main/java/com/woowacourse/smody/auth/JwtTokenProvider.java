package com.woowacourse.smody.auth;

import com.woowacourse.smody.dto.TokenPayload;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import java.nio.charset.StandardCharsets;
import java.util.Date;
import javax.crypto.SecretKey;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class JwtTokenProvider {

    private static final String SECRET_KEY = "${security.jwt.token.secret-key}";
    private static final String EXPIRE_LENGTH = "${security.jwt.token.expire-length}";

    private final SecretKey key;
    private final long validityInMilliseconds;

    public JwtTokenProvider(@Value(SECRET_KEY) final String secretKey,
                            @Value(EXPIRE_LENGTH) final long validityInMilliseconds) {
        this.key = Keys.hmacShaKeyFor(secretKey.getBytes(StandardCharsets.UTF_8));
        this.validityInMilliseconds = validityInMilliseconds;
    }

    public String createToken(TokenPayload tokenPayload) {
        Date now = new Date();
        Date validity = new Date(now.getTime() + (validityInMilliseconds));
        return Jwts.builder()
                .addClaims(tokenPayload.toMap())
                .setIssuedAt(now)
                .setExpiration(validity)
                .signWith(key, SignatureAlgorithm.HS256)
                .compact();
    }

    public TokenPayload getPayload(String token) {
        Claims claims = parseClaimsJws(token).getBody();
        Long id = claims.get("id", Long.class);
        String nickname = claims.get("nickname", String.class);
        return new TokenPayload(id, nickname);
    }

    private Jws<Claims> parseClaimsJws(final String token) {
        return Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(token);
    }

    public boolean validateToken(String token) {
        try {
            return !parseClaimsJws(token).getBody().getExpiration().before(new Date());
        } catch (JwtException | IllegalArgumentException e) {
            return false;
        }
    }
}
