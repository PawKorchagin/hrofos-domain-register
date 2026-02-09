package ru.itmo.payment.config;

import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;
import ru.itmo.payment.client.DomainClientProperties;
import ru.itmo.payment.client.YooKassaClientProperties;

@Configuration
@EnableConfigurationProperties({DomainClientProperties.class, YooKassaClientProperties.class})
public class ClientConfig {

    @Bean
    public RestTemplate restTemplate() {
        return new RestTemplate();
    }
}
