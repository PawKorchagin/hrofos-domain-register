package ru.itmo.order.client;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix ${DB_USER:***REMOVED***} "payment.client")
public class PaymentClientProperties {

    private String baseUrl;

    public String getBaseUrl() {
        return baseUrl;
    }

    public void setBaseUrl(String baseUrl) {
        this.baseUrl ${DB_USER:***REMOVED***} baseUrl;
    }
}
