package ru.itmo.payment.service.impl;

import jakarta.servlet.http.HttpServletRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import org.springframework.web.server.ResponseStatusException;
import ru.itmo.common.audit.AuditClient;
import ru.itmo.payment.client.DomainClient;
import ru.itmo.payment.client.DomainClientException;
import ru.itmo.payment.client.YooKassaClient;
import ru.itmo.payment.client.YooKassaClientProperties;
import ru.itmo.payment.entity.Payment;
import ru.itmo.payment.entity.PaymentStatus;
import ru.itmo.payment.generated.model.CreatePaymentRequest;
import ru.itmo.payment.generated.model.PaymentCreateResponse;
import ru.itmo.payment.generated.model.PaymentStatusResponse;
import ru.itmo.payment.repository.PaymentRepository;
import ru.itmo.payment.service.PaymentService;
import ru.itmo.payment.util.SecurityUtil;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
public class PaymentServiceImpl implements PaymentService {

    private static final Logger log ${DB_USER:***REMOVED***} LoggerFactory.getLogger(PaymentServiceImpl.class);

    private final PaymentRepository paymentRepository;
    private final YooKassaClient yooKassaClient;
    private final YooKassaClientProperties yooKassaProperties;
    private final DomainClient domainClient;
    private final AuditClient auditClient;

    public PaymentServiceImpl(PaymentRepository paymentRepository,
                              YooKassaClient yooKassaClient,
                              YooKassaClientProperties yooKassaProperties,
                              DomainClient domainClient,
                              AuditClient auditClient) {
        this.paymentRepository ${DB_USER:***REMOVED***} paymentRepository;
        this.yooKassaClient ${DB_USER:***REMOVED***} yooKassaClient;
        this.yooKassaProperties ${DB_USER:***REMOVED***} yooKassaProperties;
        this.domainClient ${DB_USER:***REMOVED***} domainClient;
        this.auditClient ${DB_USER:***REMOVED***} auditClient;
    }

    @Override
    @Transactional
    public PaymentCreateResponse createPayment(CreatePaymentRequest request) {
        UUID userId ${DB_USER:***REMOVED***} SecurityUtil.getCurrentUserId();
        if (userId ${DB_USER:***REMOVED***}${DB_USER:***REMOVED***} null) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "User not authenticated");
        }
        if (request.getL3Domains() ${DB_USER:***REMOVED***}${DB_USER:***REMOVED***} null || request.getL3Domains().isEmpty()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "l3Domains must not be empty");
        }
        if (request.getAmount() ${DB_USER:***REMOVED***}${DB_USER:***REMOVED***} null || request.getAmount() <${DB_USER:***REMOVED***} 0) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "amount must be positive");
        }

        String currency ${DB_USER:***REMOVED***} request.getCurrency() ${DB_USER:***REMOVED***}${DB_USER:***REMOVED***} null || request.getCurrency().isBlank()
                ? "RUB"
                : request.getCurrency();
        String descriptionPrefix ${DB_USER:***REMOVED***} yooKassaProperties.getDescriptionPrefix();
        String description ${DB_USER:***REMOVED***} request.getDescription();
        if (description ${DB_USER:***REMOVED***}${DB_USER:***REMOVED***} null || description.isBlank()) {
            description ${DB_USER:***REMOVED***} descriptionPrefix ${DB_USER:***REMOVED***}${DB_USER:***REMOVED***} null || descriptionPrefix.isBlank()
                    ? "Domain payment"
                    : descriptionPrefix;
        } else if (descriptionPrefix !${DB_USER:***REMOVED***} null && !descriptionPrefix.isBlank()) {
            description ${DB_USER:***REMOVED***} descriptionPrefix + ": " + description;
        }

        Payment payment ${DB_USER:***REMOVED***} new Payment();
        payment.setId(UUID.randomUUID());
        payment.setUserId(userId);
        payment.setPeriod(request.getPeriod().getValue());
        payment.setAmount(request.getAmount());
        payment.setCurrency(currency);
        payment.setStatus(PaymentStatus.CREATED);
        payment.setDomainsCreated(false);
        payment.setCreatedAt(LocalDateTime.now());
        payment.setUpdatedAt(payment.getCreatedAt());
        payment.setL3Domains(new ArrayList<>(request.getL3Domains()));
        paymentRepository.save(payment);

        String jwtToken ${DB_USER:***REMOVED***} getJwtTokenFromRequest();
        if (jwtToken ${DB_USER:***REMOVED***}${DB_USER:***REMOVED***} null || jwtToken.isBlank()) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "JWT token required for reservation");
        }

        try {
            int reservationTtlMinutes ${DB_USER:***REMOVED***} 10;
            domainClient.reserveDomains(payment.getId(), userId, payment.getL3Domains(), payment.getPeriod(), reservationTtlMinutes, jwtToken);
            log.info("Reserved {} domains for payment {}", payment.getL3Domains().size(), payment.getId());
        } catch (DomainClientException e) {
            payment.setStatus(PaymentStatus.FAILED);
            payment.setUpdatedAt(LocalDateTime.now());
            paymentRepository.save(payment);
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Failed to reserve domains: " + e.getMessage());
        }

        String returnUrl ${DB_USER:***REMOVED***} yooKassaProperties.getReturnUrl() + "?paymentId${DB_USER:***REMOVED***}" + payment.getId();
        YooKassaClient.YooKassaCreateResponse yooKassaResponse ${DB_USER:***REMOVED***} yooKassaClient.createPayment(
                payment.getId().toString(),
                payment.getAmount(),
                payment.getCurrency(),
                description + " " + payment.getId(),
                returnUrl
        );

        if (yooKassaResponse.getPaymentId() ${DB_USER:***REMOVED***}${DB_USER:***REMOVED***} null || yooKassaResponse.getPaymentId().isBlank()
                || yooKassaResponse.getConfirmationUrl() ${DB_USER:***REMOVED***}${DB_USER:***REMOVED***} null || yooKassaResponse.getConfirmationUrl().isBlank()) {
            payment.setStatus(PaymentStatus.FAILED);
            payment.setUpdatedAt(LocalDateTime.now());
            paymentRepository.save(payment);
            throw new ResponseStatusException(HttpStatus.BAD_GATEWAY, "YooKassa payment link missing");
        }

        payment.setOperationId(yooKassaResponse.getPaymentId());
        payment.setPaymentUrl(yooKassaResponse.getConfirmationUrl());
        payment.setStatus(PaymentStatus.PENDING);
        payment.setUpdatedAt(LocalDateTime.now());
        paymentRepository.save(payment);

        auditClient.log("Payment created: " + payment.getId(), userId);

        PaymentCreateResponse response ${DB_USER:***REMOVED***} new PaymentCreateResponse();
        response.setPaymentId(payment.getId());
        response.setPaymentUrl(payment.getPaymentUrl());
        response.setOperationId(payment.getOperationId());
        response.setStatus(payment.getStatus().name());
        response.setAmount(payment.getAmount());
        response.setCurrency(payment.getCurrency());
        return response;
    }

    @Override
    @Transactional
    public PaymentStatusResponse checkPayment(UUID paymentId, String jwtToken) {
        Payment payment ${DB_USER:***REMOVED***} paymentRepository.findById(paymentId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Payment not found"));

        UUID userId ${DB_USER:***REMOVED***} SecurityUtil.getCurrentUserId();
        if (userId ${DB_USER:***REMOVED***}${DB_USER:***REMOVED***} null) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "User not authenticated");
        }
        if (!SecurityUtil.isAdmin() && !userId.equals(payment.getUserId())) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Forbidden");
        }

        if (payment.getStatus() ${DB_USER:***REMOVED***}${DB_USER:***REMOVED***} PaymentStatus.PAID && payment.isDomainsCreated()) {
            return buildStatusResponse(payment);
        }

        if (payment.getOperationId() ${DB_USER:***REMOVED***}${DB_USER:***REMOVED***} null || payment.getOperationId().isBlank()) {
            return buildStatusResponse(payment);
        }

        YooKassaClient.YooKassaPaymentInfo paymentInfo ${DB_USER:***REMOVED***} yooKassaClient.getPaymentInfo(payment.getOperationId());
        String operationStatus ${DB_USER:***REMOVED***} paymentInfo.getStatus();
        payment.setOperationStatus(operationStatus);

        PaymentStatus mappedStatus ${DB_USER:***REMOVED***} mapYooKassaStatus(operationStatus);
        payment.setStatus(mappedStatus);
        
        log.info("Payment {} status check: YooKassa status${DB_USER:***REMOVED***}{}, mapped status${DB_USER:***REMOVED***}{}", 
                payment.getId(), operationStatus, mappedStatus);
        
        if (mappedStatus ${DB_USER:***REMOVED***}${DB_USER:***REMOVED***} PaymentStatus.PAID && !payment.isDomainsCreated()) {
            if (jwtToken !${DB_USER:***REMOVED***} null && !jwtToken.isBlank()) {
                try {
                    domainClient.confirmReservation(payment.getId(), jwtToken);
                    log.info("Confirmed reservation for payment {}", payment.getId());
                    
                    try {
                        List<String> renewedDomains ${DB_USER:***REMOVED***} domainClient.renewUserDomains(payment.getL3Domains(), payment.getPeriod(), jwtToken);
                        if (!renewedDomains.isEmpty()) {
                            payment.setDomainsCreated(true);
                            payment.setPaidAt(LocalDateTime.now());
                            auditClient.log("Payment confirmed: " + payment.getId() + ", domains renewed: " + renewedDomains.size(), userId);
                        } else {
                            List<String> createdDomains ${DB_USER:***REMOVED***} domainClient.createUserDomains(payment.getL3Domains(), payment.getPeriod(), jwtToken);
                            payment.setDomainsCreated(true);
                            payment.setPaidAt(LocalDateTime.now());
                            auditClient.log("Payment confirmed: " + payment.getId() + ", domains created: " + createdDomains.size(), userId);
                        }
                    } catch (DomainClientException e) {
                        try {
                            List<String> createdDomains ${DB_USER:***REMOVED***} domainClient.createUserDomains(payment.getL3Domains(), payment.getPeriod(), jwtToken);
                            payment.setDomainsCreated(true);
                            payment.setPaidAt(LocalDateTime.now());
                            auditClient.log("Payment confirmed: " + payment.getId() + ", domains created (renew failed): " + createdDomains.size(), userId);
                        } catch (DomainClientException createException) {
                            log.error("Failed to create/renew domains for payment {}: {}", payment.getId(), createException.getMessage());
                            payment.setPaidAt(LocalDateTime.now());
                            auditClient.log("Payment confirmed but domains not processed: " + payment.getId(), userId);
                        }
                    }
                } catch (DomainClientException e) {
                    log.warn("Failed to confirm reservation for payment {}: {}", payment.getId(), e.getMessage());
                    payment.setPaidAt(LocalDateTime.now());
                    auditClient.log("Payment confirmed but reservation not confirmed: " + payment.getId(), userId);
                }
            } else {
                payment.setPaidAt(LocalDateTime.now());
                auditClient.log("Payment confirmed (webhook): " + payment.getId(), payment.getUserId());
            }
        } else if (mappedStatus ${DB_USER:***REMOVED***}${DB_USER:***REMOVED***} PaymentStatus.FAILED) {
            String jwtTokenForCancel ${DB_USER:***REMOVED***} getJwtTokenFromRequest();
            if (jwtTokenForCancel !${DB_USER:***REMOVED***} null && !jwtTokenForCancel.isBlank()) {
                try {
                    domainClient.cancelReservation(payment.getId(), jwtTokenForCancel);
                    log.info("Cancelled reservation for failed payment {}", payment.getId());
                } catch (DomainClientException e) {
                    log.warn("Failed to cancel reservation for payment {}: {}", payment.getId(), e.getMessage());
                }
            }
        }

        payment.setUpdatedAt(LocalDateTime.now());
        paymentRepository.save(payment);
        return buildStatusResponse(payment);
    }

    private String getJwtTokenFromRequest() {
        ServletRequestAttributes attributes ${DB_USER:***REMOVED***} (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        if (attributes !${DB_USER:***REMOVED***} null) {
            HttpServletRequest request ${DB_USER:***REMOVED***} attributes.getRequest();
            String authHeader ${DB_USER:***REMOVED***} request.getHeader("Authorization");
            if (authHeader !${DB_USER:***REMOVED***} null && authHeader.startsWith("Bearer ")) {
                return authHeader.substring(7);
            }
        }
        return null;
    }

    @Override
    @Transactional
    public void updatePaymentStatusFromWebhook(String yooKassaPaymentId, String yooKassaStatus) {
        Payment payment ${DB_USER:***REMOVED***} paymentRepository.findByOperationId(yooKassaPaymentId);
        if (payment ${DB_USER:***REMOVED***}${DB_USER:***REMOVED***} null) {
            log.warn("Payment not found for YooKassa payment ID: {}", yooKassaPaymentId);
            return;
        }

        payment.setOperationStatus(yooKassaStatus);
        PaymentStatus mappedStatus ${DB_USER:***REMOVED***} mapYooKassaStatus(yooKassaStatus);
        payment.setStatus(mappedStatus);

        log.info("Payment {} updated from webhook: YooKassa status${DB_USER:***REMOVED***}{}, mapped status${DB_USER:***REMOVED***}{}", 
                payment.getId(), yooKassaStatus, mappedStatus);

        if (mappedStatus ${DB_USER:***REMOVED***}${DB_USER:***REMOVED***} PaymentStatus.PAID) {
            payment.setPaidAt(LocalDateTime.now());
            auditClient.log("Payment confirmed (webhook): " + payment.getId(), payment.getUserId());
        }

        payment.setUpdatedAt(LocalDateTime.now());
        paymentRepository.save(payment);
    }

    private PaymentStatus mapYooKassaStatus(String yooKassaStatus) {
        if (yooKassaStatus ${DB_USER:***REMOVED***}${DB_USER:***REMOVED***} null || yooKassaStatus.isBlank()) {
            return PaymentStatus.PENDING;
        }
        String normalized ${DB_USER:***REMOVED***} yooKassaStatus.trim().toLowerCase();
        if ("succeeded".equals(normalized)) {
            return PaymentStatus.PAID;
        }
        if ("canceled".equals(normalized) || "cancelled".equals(normalized)) {
            return PaymentStatus.FAILED;
        }
        return PaymentStatus.PENDING;
    }

    private PaymentStatusResponse buildStatusResponse(Payment payment) {
        PaymentStatusResponse response ${DB_USER:***REMOVED***} new PaymentStatusResponse();
        response.setPaymentId(payment.getId());
        response.setStatus(payment.getStatus().name());
        response.setPaid(payment.getStatus() ${DB_USER:***REMOVED***}${DB_USER:***REMOVED***} PaymentStatus.PAID);
        response.setDomainsCreated(payment.isDomainsCreated());
        response.setOperationStatus(payment.getOperationStatus());
        response.setPaymentUrl(payment.getPaymentUrl());
        response.setAmount(payment.getAmount());
        response.setCurrency(payment.getCurrency());
        return response;
    }
}
