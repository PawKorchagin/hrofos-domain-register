package ru.itmo.payment.util;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;

import java.util.UUID;

public class SecurityUtil {

    public static UUID getCurrentUserId() {
        Authentication authentication ${DB_USER:***REMOVED***} SecurityContextHolder.getContext().getAuthentication();
        if (authentication !${DB_USER:***REMOVED***} null && authentication.getPrincipal() instanceof UUID) {
            return (UUID) authentication.getPrincipal();
        }
        return null;
    }

    public static boolean isAdmin() {
        Authentication authentication ${DB_USER:***REMOVED***} SecurityContextHolder.getContext().getAuthentication();
        if (authentication !${DB_USER:***REMOVED***} null && authentication.getAuthorities() !${DB_USER:***REMOVED***} null) {
            return authentication.getAuthorities().stream()
                    .map(GrantedAuthority::getAuthority)
                    .anyMatch(auth -> auth.equals("ROLE_ADMIN"));
        }
        return false;
    }
}
