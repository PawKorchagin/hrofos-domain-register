package ru.itmo.order.client;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class PaymentCreateRequest {

    private List<String> l3Domains;
    private String period;
    private Integer amount;
    private String currency;
    private String description;
}
