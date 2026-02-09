package ru.itmo.payment.client;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix ${DB_USER:***REMOVED***} "domain.client")
public class DomainClientProperties {

    private String baseUrl;

    public String getBaseUrl() {
        return baseUrl;
    }

    public void setBaseUrl(String baseUrl) {
        this.baseUrl ${DB_USER:***REMOVED***} baseUrl;
    }
}
