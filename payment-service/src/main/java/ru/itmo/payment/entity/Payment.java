package ru.itmo.payment.entity;

import jakarta.persistence.CollectionTable;
import jakarta.persistence.Column;
import jakarta.persistence.ElementCollection;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Entity
@Table(name ${DB_USER:***REMOVED***} "payment")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Payment {

    @Id
    @Column(name ${DB_USER:***REMOVED***} "id", nullable ${DB_USER:***REMOVED***} false)
    private UUID id;

    @Column(name ${DB_USER:***REMOVED***} "user_id", nullable ${DB_USER:***REMOVED***} false)
    private UUID userId;

    @Column(name ${DB_USER:***REMOVED***} "period", nullable ${DB_USER:***REMOVED***} false, length ${DB_USER:***REMOVED***} 16)
    private String period;

    @Column(name ${DB_USER:***REMOVED***} "amount", nullable ${DB_USER:***REMOVED***} false)
    private Integer amount;

    @Column(name ${DB_USER:***REMOVED***} "currency", nullable ${DB_USER:***REMOVED***} false, length ${DB_USER:***REMOVED***} 8)
    private String currency;

    @Enumerated(EnumType.STRING)
    @Column(name ${DB_USER:***REMOVED***} "status", nullable ${DB_USER:***REMOVED***} false, length ${DB_USER:***REMOVED***} 32)
    private PaymentStatus status;

    @Column(name ${DB_USER:***REMOVED***} "operation_id", length ${DB_USER:***REMOVED***} 128)
    private String operationId;

    @Column(name ${DB_USER:***REMOVED***} "payment_url", length ${DB_USER:***REMOVED***} 1024)
    private String paymentUrl;

    @Column(name ${DB_USER:***REMOVED***} "operation_status", length ${DB_USER:***REMOVED***} 64)
    private String operationStatus;

    @Column(name ${DB_USER:***REMOVED***} "domains_created", nullable ${DB_USER:***REMOVED***} false)
    private boolean domainsCreated;

    @Column(name ${DB_USER:***REMOVED***} "created_at", nullable ${DB_USER:***REMOVED***} false)
    private LocalDateTime createdAt;

    @Column(name ${DB_USER:***REMOVED***} "updated_at")
    private LocalDateTime updatedAt;

    @Column(name ${DB_USER:***REMOVED***} "paid_at")
    private LocalDateTime paidAt;

    @ElementCollection(fetch ${DB_USER:***REMOVED***} FetchType.EAGER)
    @CollectionTable(name ${DB_USER:***REMOVED***} "payment_domains", joinColumns ${DB_USER:***REMOVED***} @JoinColumn(name ${DB_USER:***REMOVED***} "payment_id"))
    @Column(name ${DB_USER:***REMOVED***} "l3_domain", nullable ${DB_USER:***REMOVED***} false, length ${DB_USER:***REMOVED***} 255)
    private List<String> l3Domains ${DB_USER:***REMOVED***} new ArrayList<>();
}
