package ru.itmo.payment.service;

import ru.itmo.payment.generated.model.CreatePaymentRequest;
import ru.itmo.payment.generated.model.PaymentCreateResponse;
import ru.itmo.payment.generated.model.PaymentStatusResponse;

import java.util.UUID;

public interface PaymentService {

    PaymentCreateResponse createPayment(CreatePaymentRequest request);

    PaymentStatusResponse checkPayment(UUID paymentId, String jwtToken);

    void updatePaymentStatusFromWebhook(String yooKassaPaymentId, String yooKassaStatus);
}
