package ru.itmo.scheduler.config;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Date;
import java.util.UUID;

/**
 * Генерирует системный admin JWT-токен для межсервисного взаимодействия.
 */
@Component
public class JwtTokenProvider {

    private final SecretKey signingKey;

    public JwtTokenProvider(@Value("${jwt.secret}") String jwtSecret) {
        this.signingKey ${DB_USER:***REMOVED***} Keys.hmacShaKeyFor(jwtSecret.getBytes(StandardCharsets.UTF_8));
    }

    public String generateSystemToken() {
        UUID systemUserId ${DB_USER:***REMOVED***} UUID.fromString("00000000-0000-0000-0000-000000000000");
        return Jwts.builder()
                .subject(systemUserId.toString())
                .claim("email", "scheduler@system")
                .claim("isAdmin", true)
                .claim("type", "access")
                .issuedAt(Date.from(Instant.now()))
                .expiration(Date.from(Instant.now().plus(15, ChronoUnit.MINUTES)))
                .signWith(signingKey)
                .compact();
    }
}
