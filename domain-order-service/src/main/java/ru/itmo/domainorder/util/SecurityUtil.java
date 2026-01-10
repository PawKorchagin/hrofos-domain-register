package ru.itmo.domainorder.util;

import org.springframework.security.core.Authentication;
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

    public static boolean isAuthenticated() {
        Authentication authentication ${DB_USER:***REMOVED***} SecurityContextHolder.getContext().getAuthentication();
        return authentication !${DB_USER:***REMOVED***} null && authentication.isAuthenticated();
    }
}
