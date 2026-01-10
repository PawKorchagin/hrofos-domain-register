package ru.itmo.notification.util;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import ru.itmo.notification.security.UserInfo;

import java.util.UUID;

public class SecurityUtil {

    public static UUID getCurrentUserId() {
        Authentication authentication ${DB_USER:***REMOVED***} SecurityContextHolder.getContext().getAuthentication();
        if (authentication !${DB_USER:***REMOVED***} null && authentication.getPrincipal() instanceof UserInfo) {
            return ((UserInfo) authentication.getPrincipal()).getUserId();
        }
        return null;
    }

    public static String getCurrentUserEmail() {
        Authentication authentication ${DB_USER:***REMOVED***} SecurityContextHolder.getContext().getAuthentication();
        if (authentication !${DB_USER:***REMOVED***} null && authentication.getPrincipal() instanceof UserInfo) {
            return ((UserInfo) authentication.getPrincipal()).getEmail();
        }
        return null;
    }

    public static boolean isAuthenticated() {
        Authentication authentication ${DB_USER:***REMOVED***} SecurityContextHolder.getContext().getAuthentication();
        return authentication !${DB_USER:***REMOVED***} null && authentication.isAuthenticated();
    }

    public static boolean isAdmin() {
        Authentication authentication ${DB_USER:***REMOVED***} SecurityContextHolder.getContext().getAuthentication();
        if (authentication !${DB_USER:***REMOVED***} null && authentication.getAuthorities() !${DB_USER:***REMOVED***} null) {
            return authentication.getAuthorities().stream()
                    .map(GrantedAuthority::getAuthority)
                    .anyMatch(authority -> authority.equals("ROLE_ADMIN"));
        }
        return false;
    }
}
