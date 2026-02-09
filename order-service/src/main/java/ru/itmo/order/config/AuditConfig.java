package ru.itmo.order.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;
import ru.itmo.common.audit.AuditClient;

@Configuration
public class AuditConfig {

    @Value("${audit.client.base-url}")
    private String auditBaseUrl;

    @Bean
    public AuditClient auditClient(RestTemplate restTemplate) {
        return new AuditClient(restTemplate, auditBaseUrl);
    }
}
