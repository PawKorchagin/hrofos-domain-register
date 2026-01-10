package ru.itmo.notification.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import ru.itmo.common.security.JwtUtil;

@Configuration
public class JwtConfig {

    @Value("${jwt.secret:your-256-bit-secret-key-must-be-at-least-32-characters-long-for-security}")
    private String jwtSecret;

    @Bean
    public JwtUtil jwtUtil() {
        return new JwtUtil(jwtSecret);
    }
}
