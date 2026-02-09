package ru.itmo.order.security;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import ru.itmo.common.security.JwtUtil;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Component
@RequiredArgsConstructor
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final JwtUtil jwtUtil;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {
        String authHeader ${DB_USER:***REMOVED***} request.getHeader("Authorization");

        if (authHeader ${DB_USER:***REMOVED***}${DB_USER:***REMOVED***} null || !authHeader.startsWith("Bearer ")) {
            filterChain.doFilter(request, response);
            return;
        }

        String token ${DB_USER:***REMOVED***} authHeader.substring(7);

        try {
            if (jwtUtil.validateToken(token, "access")) {
                UUID userId ${DB_USER:***REMOVED***} jwtUtil.getUserIdFromToken(token);
                Boolean isAdmin ${DB_USER:***REMOVED***} jwtUtil.getIsAdminFromToken(token);

                List<SimpleGrantedAuthority> authorities ${DB_USER:***REMOVED***} new ArrayList<>();
                authorities.add(new SimpleGrantedAuthority("ROLE_USER"));
                if (Boolean.TRUE.equals(isAdmin)) {
                    authorities.add(new SimpleGrantedAuthority("ROLE_ADMIN"));
                }

                UsernamePasswordAuthenticationToken authentication ${DB_USER:***REMOVED***} new UsernamePasswordAuthenticationToken(
                        userId,
                        null,
                        authorities
                );
                authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                SecurityContextHolder.getContext().setAuthentication(authentication);
            }
        } catch (Exception ignored) {
            // leave context empty, will result in 403 for protected endpoints
        }

        filterChain.doFilter(request, response);
    }
}
