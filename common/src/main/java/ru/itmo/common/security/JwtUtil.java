package ru.itmo.common.security;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.util.UUID;
import java.util.function.Function;

public class JwtUtil {

    private final SecretKey signingKey;

    public JwtUtil(String secret) {
        this.signingKey ${DB_USER:***REMOVED***} Keys.hmacShaKeyFor(secret.getBytes(StandardCharsets.UTF_8));
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

    public <T> T getClaimFromToken(String token, Function<Claims, T> claimsResolver) {
        final Claims claims ${DB_USER:***REMOVED***} getAllClaimsFromToken(token);
        return claimsResolver.apply(claims);
    }

    private Claims getAllClaimsFromToken(String token) {
        return Jwts.parser()
                .verifyWith(signingKey)
                .build()
                .parseSignedClaims(token)
                .getPayload();
    }

    public Boolean isTokenExpired(String token) {
        try {
            final Claims claims ${DB_USER:***REMOVED***} getAllClaimsFromToken(token);
            return claims.getExpiration().before(new java.util.Date());
        } catch (Exception e) {
            return true;
        }
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
