package ru.itmo.auth.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.web.client.RestTemplate;
import ru.itmo.auth.dto.LoginRequest;
import ru.itmo.auth.dto.LoginResponse;
import ru.itmo.auth.dto.RefreshTokenRequest;
import ru.itmo.auth.dto.RegisterRequest;
import ru.itmo.auth.dto.UserResponse;
import ru.itmo.auth.entity.RefreshToken;
import ru.itmo.auth.entity.User;
import ru.itmo.auth.exception.EmailAlreadyExistsException;
import ru.itmo.auth.exception.InvalidCredentialsException;
import ru.itmo.auth.exception.InvalidTOTPException;
import ru.itmo.auth.exception.InvalidTokenException;
import ru.itmo.auth.exception.TOTPRequiredException;
import ru.itmo.auth.exception.UserNotFoundException;
import ru.itmo.auth.repository.RefreshTokenRepository;
import ru.itmo.auth.repository.UserRepository;
import ru.itmo.auth.util.JwtUtil;
import ru.itmo.auth.service.TwoFactorService;
import ru.itmo.common.notification.NotificationType;
import ru.itmo.common.notification.SendNotificationRequest;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Slf4j
public class AuthService {

    private final UserRepository userRepository;
    private final RefreshTokenRepository refreshTokenRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtil jwtUtil;
    private final RestTemplate restTemplate;
    private final TwoFactorService twoFactorService;
    
    @Value("${services.notification.url}")
    private String notificationServiceUrl;
    
    @Value("${services.api-gateway.url}")
    private String apiGatewayUrl;
    
    @Value("${verification.base-url}")
    private String verificationBaseUrl;
    
    @Value("${jwt.refresh-token-expiration-days:30}")
    private Long refreshTokenExpirationDays;

    @Transactional
    public User register(RegisterRequest request) {
        log.info("Register request for email: {}", request.getEmail());

        if (userRepository.existsByEmail(request.getEmail())) {
            throw new EmailAlreadyExistsException("Email already exists: " + request.getEmail());
        }

        User user ${DB_USER:***REMOVED***} new User();
        user.setEmail(request.getEmail());
        user.setPasswordHash(passwordEncoder.encode(request.getPassword()));
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
            parameters.put("verificationLink", verificationLink);

            SendNotificationRequest notificationRequest ${DB_USER:***REMOVED***} new SendNotificationRequest();
            notificationRequest.setUserId(user.getId());
            notificationRequest.setType(NotificationType.EMAIL_VERIFICATION);
            notificationRequest.setSubject("Подтверждение email адреса");
            notificationRequest.setParameters(parameters);

            String jwtToken ${DB_USER:***REMOVED***} jwtUtil.generateAccessToken(user.getId(), user.getEmail(), user.getIsAdmin());

            HttpHeaders headers ${DB_USER:***REMOVED***} new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);
            headers.setBearerAuth(jwtToken);

            HttpEntity<SendNotificationRequest> requestEntity ${DB_USER:***REMOVED***} new HttpEntity<>(notificationRequest, headers);

            String url ${DB_USER:***REMOVED***} notificationServiceUrl + "/notifications/send";
            restTemplate.exchange(url, HttpMethod.POST, requestEntity, Void.class);
            log.info("Verification email sent to: {} with JWT token in Authorization header", user.getEmail());
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

    @Transactional
    public LoginResponse login(LoginRequest request) {
        log.info("Login request for email: {}", request.getEmail());

        User user ${DB_USER:***REMOVED***} userRepository.findByEmail(request.getEmail())
                .orElseThrow(() -> new InvalidCredentialsException("Invalid email or password"));

        if (!passwordEncoder.matches(request.getPassword(), user.getPasswordHash())) {
            throw new InvalidCredentialsException("Invalid email or password");
        }

        if (twoFactorService.is2FAEnabled(user.getId())) {
            if (request.getTotpCode() ${DB_USER:***REMOVED***}${DB_USER:***REMOVED***} null || request.getTotpCode().isEmpty()) {
                throw new TOTPRequiredException("TOTP code is required");
            }

            if (!twoFactorService.verify2FA(user.getId(), request.getTotpCode())) {
                throw new InvalidTOTPException("Invalid TOTP code");
            }
        }

        String accessToken ${DB_USER:***REMOVED***} jwtUtil.generateAccessToken(user.getId(), user.getEmail(), user.getIsAdmin());
        String refreshTokenString ${DB_USER:***REMOVED***} jwtUtil.generateRefreshToken(user.getId());

        refreshTokenRepository.deleteByUserId(user.getId());

        RefreshToken refreshToken ${DB_USER:***REMOVED***} new RefreshToken();
        refreshToken.setUserId(user.getId());
        refreshToken.setToken(refreshTokenString);
        refreshToken.setExpiresAt(LocalDateTime.now().plusDays(refreshTokenExpirationDays));
        refreshTokenRepository.save(refreshToken);

        log.info("User logged in successfully: {}", user.getId());

        LoginResponse response ${DB_USER:***REMOVED***} new LoginResponse();
        response.setUserId(user.getId());
        response.setAccessToken(accessToken);
        response.setRefreshToken(refreshTokenString);
        response.setEmail(user.getEmail());

        return response;
    }

    @Transactional
    public LoginResponse refreshToken(RefreshTokenRequest request) {
        log.info("Refresh token request");

        if (!jwtUtil.validateToken(request.getRefreshToken(), "refresh")) {
            throw new InvalidTokenException("Invalid refresh token");
        }

        RefreshToken refreshToken ${DB_USER:***REMOVED***} refreshTokenRepository.findByToken(request.getRefreshToken())
                .orElseThrow(() -> new InvalidTokenException("Refresh token not found"));

        if (refreshToken.getExpiresAt().isBefore(LocalDateTime.now())) {
            refreshTokenRepository.delete(refreshToken);
            throw new InvalidTokenException("Refresh token expired");
        }

        UUID userId ${DB_USER:***REMOVED***} jwtUtil.getUserIdFromToken(request.getRefreshToken());
        User user ${DB_USER:***REMOVED***} userRepository.findById(userId)
                .orElseThrow(() -> new UserNotFoundException("User not found"));

        String newAccessToken ${DB_USER:***REMOVED***} jwtUtil.generateAccessToken(user.getId(), user.getEmail(), user.getIsAdmin());
        String newRefreshTokenString ${DB_USER:***REMOVED***} jwtUtil.generateRefreshToken(user.getId());

        refreshTokenRepository.delete(refreshToken);

        RefreshToken newRefreshToken ${DB_USER:***REMOVED***} new RefreshToken();
        newRefreshToken.setUserId(user.getId());
        newRefreshToken.setToken(newRefreshTokenString);
        newRefreshToken.setExpiresAt(LocalDateTime.now().plusDays(refreshTokenExpirationDays));
        refreshTokenRepository.save(newRefreshToken);

        log.info("Tokens refreshed for user: {}", user.getId());

        LoginResponse response ${DB_USER:***REMOVED***} new LoginResponse();
        response.setUserId(user.getId());
        response.setAccessToken(newAccessToken);
        response.setRefreshToken(newRefreshTokenString);
        response.setEmail(user.getEmail());

        return response;
    }

    private UserResponse mapToUserResponse(User user) {
        UserResponse response ${DB_USER:***REMOVED***} new UserResponse();
        response.setId(user.getId());
        response.setEmail(user.getEmail());
        response.setCreatedAt(user.getCreatedAt());
        response.setUpdatedAt(user.getUpdatedAt());
        return response;
    }
}
