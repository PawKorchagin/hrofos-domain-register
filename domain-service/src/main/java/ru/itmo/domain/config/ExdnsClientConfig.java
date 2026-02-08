package ru.itmo.domain.config;

import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;
import ru.itmo.domain.client.ExdnsClientProperties;

@Configuration
@EnableConfigurationProperties(ExdnsClientProperties.class)
public class ExdnsClientConfig {

    @Bean
    public RestTemplate restTemplate() {
        return new RestTemplate();
    }
}
