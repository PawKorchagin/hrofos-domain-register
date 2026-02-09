package ru.itmo.domain.config;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import ru.itmo.domain.security.JwtAuthenticationFilter;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {

    private final JwtAuthenticationFilter jwtAuthenticationFilter;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .csrf(csrf -> csrf.disable())
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .authorizeHttpRequests(auth -> auth
                        // Public endpoints
                        .requestMatchers("/actuator/**", "/health", "/v3/api-docs/**", "/swagger-ui/**", "/swagger-ui.html").permitAll()
                        .requestMatchers("/domains/l3Domains/*/free").permitAll()
                        // GET l2Domains is public
                        .requestMatchers(org.springframework.http.HttpMethod.GET, "/domains/l2Domains").permitAll()
                        // Admin only endpoints
                        .requestMatchers(org.springframework.http.HttpMethod.POST, "/domains/l2Domains").hasRole("ADMIN")
                        .requestMatchers(org.springframework.http.HttpMethod.DELETE, "/domains/l2Domains/**").hasRole("ADMIN")
                        .requestMatchers(org.springframework.http.HttpMethod.POST, "/domains/l2Domains/**").hasRole("ADMIN") // createDnsRecord
                        .requestMatchers(org.springframework.http.HttpMethod.GET, "/domains/l2Domains/**").hasRole("ADMIN") // getDnsRecords
                        .requestMatchers(org.springframework.http.HttpMethod.GET, "/domains/dnsRecords/**").hasRole("ADMIN")
                        .requestMatchers(org.springframework.http.HttpMethod.GET, "/domains/stats").hasRole("ADMIN")
                        // Authenticated endpoints (users can modify their own, admins can modify all)
                        .requestMatchers(org.springframework.http.HttpMethod.POST, "/domains/userDomains").authenticated()
                        .requestMatchers(org.springframework.http.HttpMethod.PUT, "/domains/dnsRecords/**").authenticated()
                        .requestMatchers(org.springframework.http.HttpMethod.DELETE, "/domains/dnsRecords/**").authenticated()
                        // Users can create L3 domains
                        .requestMatchers("/domains/l3Domains/**").authenticated()
                        .anyRequest().permitAll()
                )
                .addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class);
        return http.build();
    }
}
