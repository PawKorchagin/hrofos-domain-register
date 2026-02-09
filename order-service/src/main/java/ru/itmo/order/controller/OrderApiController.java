package ru.itmo.order.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;
import ru.itmo.order.generated.api.OrderApi;
import ru.itmo.order.generated.model.Order;

import java.util.List;

@RestController
@org.springframework.web.bind.annotation.RequestMapping("${openapi.orderService.base-path:/orders}")
public class OrderApiController implements OrderApi {

    @Override
    public ResponseEntity<List<Order>> getOrders() {
        return ResponseEntity.ok(List.of());
    }
}
