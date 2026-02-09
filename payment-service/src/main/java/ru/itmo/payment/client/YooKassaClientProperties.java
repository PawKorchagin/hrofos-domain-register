package ru.itmo.payment.client;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix ${DB_USER:***REMOVED***} "yookassa.client")
public class YooKassaClientProperties {

    private String shopId;
    private String secretKey;
    private String returnUrl;
    private String descriptionPrefix;

    public String getShopId() {
        return shopId;
    }

    public void setShopId(String shopId) {
        this.shopId ${DB_USER:***REMOVED***} shopId;
    }

    public String getSecretKey() {
        return secretKey;
    }

    public void setSecretKey(String secretKey) {
        this.secretKey ${DB_USER:***REMOVED***} secretKey;
    }

    public String getReturnUrl() {
        return returnUrl;
    }

    public void setReturnUrl(String returnUrl) {
        this.returnUrl ${DB_USER:***REMOVED***} returnUrl;
    }

    public String getDescriptionPrefix() {
        return descriptionPrefix;
    }

    public void setDescriptionPrefix(String descriptionPrefix) {
        this.descriptionPrefix ${DB_USER:***REMOVED***} descriptionPrefix;
    }
}
