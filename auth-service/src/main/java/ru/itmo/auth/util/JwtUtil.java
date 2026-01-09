package ru.itmo.auth.util;

import io.jsonwebtoken.Claims;
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
import java.util.function.Function;

@Component
public class JwtUtil {

    @Value("${jwt.secret:your-256-bit-secret-key-must-be-at-least-32-characters-long}")
    private String secret;

    @Value("${jwt.access-token-expiration-minutes:15}")
    private Long accessTokenExpirationMinutes;

    @Value("${jwt.refresh-token-expiration-days:30}")
    private Long refreshTokenExpirationDays;

    private SecretKey getSigningKey() {
        return Keys.hmacShaKeyFor(secret.getBytes(StandardCharsets.UTF_8));
    }

    public String generateAccessToken(UUID userId, String email, Boolean isAdmin) {
        return Jwts.builder()
                .subject(userId.toString())
                .claim("email", email)
                .claim("isAdmin", isAdmin)
                .claim("type", "access")
                .issuedAt(Date.from(Instant.now()))
                .expiration(Date.from(Instant.now().plus(accessTokenExpirationMinutes, ChronoUnit.MINUTES)))
                .signWith(getSigningKey())
                .compact();
    }

    public String generateRefreshToken(UUID userId) {
        return Jwts.builder()
                .subject(userId.toString())
                .claim("type", "refresh")
                .issuedAt(Date.from(Instant.now()))
                .expiration(Date.from(Instant.now().plus(refreshTokenExpirationDays, ChronoUnit.DAYS)))
                .signWith(getSigningKey())
                .compact();
    }

    public UUID getUserIdFromToken(String token) {
        return UUID.fromString(getClaimFromToken(token, Claims::getSubject));
    }

    public String getEmailFromToken(String token) {
        return getClaimFromToken(token, claims -> claims.get("email", String.class));
    }

    public Boolean getIsAdminFromToken(String token) {
        return getClaimFromToken(token, claims -> {
            Object isAdmin ${DB_USER:***REMOVED***} claims.get("isAdmin");
            if (isAdmin ${DB_USER:***REMOVED***}${DB_USER:***REMOVED***} null) {
                // Fallback to "admin" for backward compatibility
                Object admin ${DB_USER:***REMOVED***} claims.get("admin");
                if (admin ${DB_USER:***REMOVED***}${DB_USER:***REMOVED***} null) {
                    return false;
                }
                if (admin instanceof Boolean) {
                    return (Boolean) admin;
                }
                if (admin instanceof String) {
                    return Boolean.parseBoolean((String) admin);
                }
                return false;
            }
            if (isAdmin instanceof Boolean) {
                return (Boolean) isAdmin;
            }
            if (isAdmin instanceof String) {
                return Boolean.parseBoolean((String) isAdmin);
            }
            return false;
        });
    }

    public String getTokenType(String token) {
        return getClaimFromToken(token, claims -> claims.get("type", String.class));
    }

    public Date getExpirationDateFromToken(String token) {
        return getClaimFromToken(token, Claims::getExpiration);
    }

    public <T> T getClaimFromToken(String token, Function<Claims, T> claimsResolver) {
        final Claims claims ${DB_USER:***REMOVED***} getAllClaimsFromToken(token);
        return claimsResolver.apply(claims);
    }

    private Claims getAllClaimsFromToken(String token) {
        return Jwts.parser()
                .verifyWith(getSigningKey())
                .build()
                .parseSignedClaims(token)
                .getPayload();
    }

    public Boolean isTokenExpired(String token) {
        final Date expiration ${DB_USER:***REMOVED***} getExpirationDateFromToken(token);
        return expiration.before(Date.from(Instant.now()));
    }

    public Boolean validateToken(String token, String expectedType) {
        try {
            String type ${DB_USER:***REMOVED***} getTokenType(token);
            return type !${DB_USER:***REMOVED***} null && type.equals(expectedType) && !isTokenExpired(token);
        } catch (Exception e) {
            return false;
        }
    }
}
