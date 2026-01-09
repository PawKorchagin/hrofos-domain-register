package ru.itmo.auth.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;
import ru.itmo.auth.dto.LoginRequest;
import ru.itmo.auth.dto.LoginResponse;
import ru.itmo.auth.dto.RegisterRequest;
import ru.itmo.auth.dto.UserResponse;
import ru.itmo.auth.entity.User;
import ru.itmo.auth.exception.EmailAlreadyExistsException;
import ru.itmo.auth.exception.UserNotFoundException;
import ru.itmo.auth.repository.UserRepository;
import ru.itmo.common.notification.NotificationType;
import ru.itmo.common.notification.SendNotificationRequest;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Slf4j
public class AuthService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final RestTemplate restTemplate;
    
    @Value("${services.notification.url}")
    private String notificationServiceUrl;
    
    @Value("${services.api-gateway.url}")
    private String apiGatewayUrl;
    
    @Value("${verification.base-url}")
    private String verificationBaseUrl;

    @Transactional
    public User register(RegisterRequest request) {
        log.info("Register request for email: {}", request.getEmail());

        if (userRepository.existsByEmail(request.getEmail())) {
            throw new EmailAlreadyExistsException("Email already exists: " + request.getEmail());
        }

        User user ${DB_USER:***REMOVED***} new User();
        user.setEmail(request.getEmail());
        user.setPasswordHash(passwordEncoder.encode(request.getPassword()));
        user.setFirstName(request.getFirstName());
        user.setLastName(request.getLastName());
        user.setEmailVerified(false);
        user.setVerificationToken(UUID.randomUUID());

        user ${DB_USER:***REMOVED***} userRepository.save(user);
        log.info("User created with id: {}", user.getId());

        return user;
    }

    public UserResponse registerAndSendVerificationEmail(RegisterRequest request) {
        User user ${DB_USER:***REMOVED***} register(request);
        sendVerificationEmail(user);
        return mapToUserResponse(user);
    }

    private void sendVerificationEmail(User user) {
        try {
            String verificationLink ${DB_USER:***REMOVED***} verificationBaseUrl + "/api/auth/verify-email?token${DB_USER:***REMOVED***}" + user.getVerificationToken();

            Map<String, Object> parameters ${DB_USER:***REMOVED***} new HashMap<>();
            parameters.put("firstName", user.getFirstName());
            parameters.put("verificationLink", verificationLink);

            SendNotificationRequest notificationRequest ${DB_USER:***REMOVED***} new SendNotificationRequest();
            notificationRequest.setUserId(user.getId());
            notificationRequest.setType(NotificationType.EMAIL_VERIFICATION);
            notificationRequest.setSubject("Подтверждение email адреса");
            notificationRequest.setParameters(parameters);

            String url ${DB_USER:***REMOVED***} notificationServiceUrl + "/notifications/send";
            restTemplate.postForObject(url, notificationRequest, Void.class);
            log.info("Verification email sent to: {}", user.getEmail());
        } catch (Exception e) {
            log.error("Failed to send verification email to: {}", user.getEmail(), e);
        }
    }

    @Transactional
    public void verifyEmail(UUID verificationToken) {
        log.info("Verifying email with token: {}", verificationToken);

        User user ${DB_USER:***REMOVED***} userRepository.findByVerificationToken(verificationToken)
                .orElseThrow(() -> new UserNotFoundException("Invalid verification token"));

        if (user.getEmailVerified()) {
            log.warn("Email already verified for user: {}", user.getId());
            return;
        }

        user.setEmailVerified(true);
        userRepository.save(user);
        log.info("Email verified for user: {}", user.getId());
    }

    @Transactional
    public void resendVerificationEmail(String email) {
        log.info("Resending verification email to: {}", email);

        User user ${DB_USER:***REMOVED***} userRepository.findByEmail(email)
                .orElseThrow(() -> new UserNotFoundException("User not found with email: " + email));

        if (user.getEmailVerified()) {
            log.warn("Email already verified for user: {}", user.getId());
            throw new IllegalStateException("Email is already verified");
        }

        user.setVerificationToken(UUID.randomUUID());
        userRepository.save(user);

        sendVerificationEmail(user);
        log.info("Verification email resent to: {}", email);
    }

    // TODO: Implement login logic
    public LoginResponse login(LoginRequest request) {
        log.info("Login request for email: {}", request.getEmail());
        throw new UnsupportedOperationException("Login not implemented yet");
    }

    private UserResponse mapToUserResponse(User user) {
        UserResponse response ${DB_USER:***REMOVED***} new UserResponse();
        response.setId(user.getId());
        response.setEmail(user.getEmail());
        response.setFirstName(user.getFirstName());
        response.setLastName(user.getLastName());
        response.setCreatedAt(user.getCreatedAt());
        response.setUpdatedAt(user.getUpdatedAt());
        return response;
    }
}
