package dev.rokku.schedule.domain.security.util;

import dev.rokku.schedule.domain.exception.ApiException;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import java.time.Instant;
import java.util.Date;

@Component
public class JwtUtil extends DefaultJwtSecret {

    @Value("${jwt.expirationMs}")
    private int jwtExpirationMs;

    public String generateJwtToken(UserDetails userDetails) {
        UserDetailsImpl usuario = (UserDetailsImpl) userDetails;

        String idUsuario = String.valueOf(usuario.getId());

        return Jwts.builder()
                .subject(idUsuario)
                .claim("user", usuario.getUsername())
                .claim("name", usuario.getName())
                .claim("authorities", usuario.getAuthorities())
                .issuedAt(new Date())
                .expiration(new Date((new Date()).getTime() + jwtExpirationMs))
                .signWith(getSecretKey())
                .compact();
    }

    public String getIdUsuarioFromToken(String token) {
        return Jwts.parser()
                .verifyWith(getSecretKey())
                .build()
                .parseSignedClaims(token)
                .getPayload()
                .getSubject();
    }

    public Long getIdUsuario(String token) {
        try {
            String idUsuario = getIdUsuarioFromToken(token);

            return Long.parseLong(idUsuario);
        } catch (NumberFormatException e) {
            throw new ApiException(HttpStatus.BAD_REQUEST, "Houve um erro com o Id do usuário.");
        } catch (Exception e) {
            throw new ApiException(HttpStatus.BAD_REQUEST, "Token inválido.");
        }
    }

    public Instant getExpiresAt() {
        return Instant.now().plusMillis(jwtExpirationMs);
    }

}
