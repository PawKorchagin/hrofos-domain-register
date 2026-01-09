package ru.itmo.notification.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import ru.itmo.common.notification.NotificationType;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import java.util.Map;

@Service
@RequiredArgsConstructor
@Slf4j
public class EmailService {

    private final JavaMailSender mailSender;
    private final EmailTemplateService emailTemplateService;
    
    @Value("${spring.mail.from:noreply@hrofors.ru}")
    private String mailFrom;

    public void sendEmail(String to, String subject, String text) {
        try {
            SimpleMailMessage message ${DB_USER:***REMOVED***} new SimpleMailMessage();
            message.setTo(to);
            message.setSubject(subject);
            message.setText(text);
            message.setFrom(mailFrom);
            mailSender.send(message);
            log.info("Email sent successfully to: {}", to);
        } catch (Exception e) {
            log.error("Failed to send email to: {}", to, e);
            throw new EmailSendingException("Failed to send email", e);
        }
    }

    public void sendHtmlEmail(String to, String subject, String htmlContent) {
        try {
            MimeMessage message ${DB_USER:***REMOVED***} mailSender.createMimeMessage();
            MimeMessageHelper helper ${DB_USER:***REMOVED***} new MimeMessageHelper(message, true, "UTF-8");
            helper.setTo(to);
            helper.setSubject(subject);
            helper.setText(htmlContent, true);
            helper.setFrom(mailFrom);
            mailSender.send(message);
            log.info("HTML email sent successfully to: {}", to);
        } catch (MessagingException e) {
            log.error("Failed to send HTML email to: {}", to, e);
            throw new EmailSendingException("Failed to send HTML email", e);
        }
    }

    public void sendNotification(String to, NotificationType type, String subject, Map<String, Object> parameters) {
        try {
            String templateName ${DB_USER:***REMOVED***} emailTemplateService.getTemplateName(type);
            String htmlContent ${DB_USER:***REMOVED***} emailTemplateService.processTemplate(templateName, parameters);
            sendHtmlEmail(to, subject, htmlContent);
            log.info("Notification email sent: type${DB_USER:***REMOVED***}{}, to${DB_USER:***REMOVED***}{}", type, to);
        } catch (Exception e) {
            log.error("Failed to send notification email: type${DB_USER:***REMOVED***}{}, to${DB_USER:***REMOVED***}{}", type, to, e);
            throw new EmailSendingException("Failed to send notification email", e);
        }
    }
}
