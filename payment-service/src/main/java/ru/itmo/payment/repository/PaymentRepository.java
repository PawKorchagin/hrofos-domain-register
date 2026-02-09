package ru.itmo.payment.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.itmo.payment.entity.Payment;

import java.util.UUID;

public interface PaymentRepository extends JpaRepository<Payment, UUID> {
    Payment findByOperationId(String operationId);
}
