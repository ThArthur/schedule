package dev.rokku.schedule.domain.security.util;

import dev.rokku.schedule.domain.exception.ApiException;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.UnsupportedJwtException;
import io.jsonwebtoken.security.Keys;
import io.jsonwebtoken.security.SignatureException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;

import javax.crypto.SecretKey;

public abstract class DefaultJwtSecret {

    @Value("${front-url}")
    protected String frontUrl;

    @Value("${jwt.secret}")
    private String jwtSecret;
    
    protected SecretKey getSecretKey() {
        String secretComClasse = jwtSecret + "_" + this.getClass().getSimpleName();
        return Keys.hmacShaKeyFor(secretComClasse.getBytes());
    }

    public String getTokenSubject(String token) {
        return Jwts.parser()
                .verifyWith(getSecretKey())
                .build()
                .parseSignedClaims(token)
                .getPayload()
                .getSubject();
    }

    public void assertValidToken(String token) {
        try {
            SecretKey key = getSecretKey();
            Jwts.parser().verifyWith(key).build().parseSignedClaims(token);
        } catch (
                SecurityException |
                MalformedJwtException |
                ExpiredJwtException |
                UnsupportedJwtException |
                IllegalArgumentException |
                SignatureException e
        ) {
            throw new ApiException(HttpStatus.UNAUTHORIZED, "Token ausente, inválido ou expirado.");
        }
    }

}