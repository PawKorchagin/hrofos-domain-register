package ru.itmo.notification.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;
import ru.itmo.common.notification.NotificationType;

import java.util.Map;

@Service
@RequiredArgsConstructor
public class EmailTemplateService {

    private final TemplateEngine templateEngine;

    public String getTemplateName(NotificationType type) {
        return switch (type) {
            case ORDER_CREATED -> "order-created";
            case PAYMENT_APPROVED -> "payment-approved";
            case DOMAIN_ACTIVATED -> "domain-activated";
            case DOMAIN_EXPIRING_SOON -> "domain-expiring-soon";
            case DOMAIN_EXPIRED -> "domain-expired";
            case DOMAIN_RENEWED -> "domain-renewed";
            case EMAIL_VERIFICATION -> "email-verification";
        };
    }

    public String processTemplate(String templateName, Map<String, Object> parameters) {
        Context context ${DB_USER:***REMOVED***} new Context();
        if (parameters !${DB_USER:***REMOVED***} null) {
            parameters.forEach(context::setVariable);
        }
        return templateEngine.process("emails/" + templateName, context);
    }
}
