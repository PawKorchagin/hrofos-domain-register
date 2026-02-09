package ru.itmo.order.client;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
public class PaymentCreateResponse {

    private UUID paymentId;
    private String operationId;
    private String paymentUrl;
    private String status;
    private Integer amount;
    private String currency;
}
