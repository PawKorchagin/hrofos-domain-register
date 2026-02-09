package ru.itmo.payment.controller;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import ru.itmo.payment.generated.api.PaymentApi;
import ru.itmo.payment.generated.model.CreatePaymentRequest;
import ru.itmo.payment.generated.model.PaymentCreateResponse;
import ru.itmo.payment.generated.model.PaymentStatusResponse;
import ru.itmo.payment.service.PaymentService;

import java.util.UUID;

@RestController
@org.springframework.web.bind.annotation.RequestMapping("${openapi.paymentService.base-path:/payments}")
public class PaymentApiController implements PaymentApi {

    private final PaymentService paymentService;
    private final HttpServletRequest httpServletRequest;

    public PaymentApiController(PaymentService paymentService, HttpServletRequest httpServletRequest) {
        this.paymentService ${DB_USER:***REMOVED***} paymentService;
        this.httpServletRequest ${DB_USER:***REMOVED***} httpServletRequest;
    }

    @Override
    public ResponseEntity<PaymentCreateResponse> createPayment(CreatePaymentRequest createPaymentRequest) {
        PaymentCreateResponse response ${DB_USER:***REMOVED***} paymentService.createPayment(createPaymentRequest);
        return ResponseEntity.status(201).body(response);
    }

    @PostMapping
    public ResponseEntity<PaymentCreateResponse> createPaymentAlias(
            @RequestBody CreatePaymentRequest createPaymentRequest) {
        return createPayment(createPaymentRequest);
    }

    @Override
    public ResponseEntity<PaymentStatusResponse> checkPayment(UUID paymentId) {
        String authHeader ${DB_USER:***REMOVED***} httpServletRequest.getHeader("Authorization");
        String jwtToken ${DB_USER:***REMOVED***} authHeader !${DB_USER:***REMOVED***} null && authHeader.startsWith("Bearer ") ? authHeader.substring(7) : null;
        PaymentStatusResponse response ${DB_USER:***REMOVED***} paymentService.checkPayment(paymentId, jwtToken);
        return ResponseEntity.ok(response);
    }
}
