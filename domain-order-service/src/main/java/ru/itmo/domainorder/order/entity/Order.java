package ru.itmo.domainorder.order.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;
import ru.itmo.domainorder.order.enumeration.OrderStatus;

import java.math.BigInteger;
import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name ${DB_USER:***REMOVED***} "app_order")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Order {
    @Id
    @GeneratedValue(strategy ${DB_USER:***REMOVED***} GenerationType.UUID)
    private UUID id;

    @Column(name ${DB_USER:***REMOVED***} "user_id", nullable ${DB_USER:***REMOVED***} false)
    private UUID userId;

    @Enumerated(EnumType.STRING)
    @JdbcTypeCode(SqlTypes.NAMED_ENUM)
    @Column(nullable ${DB_USER:***REMOVED***} false, columnDefinition ${DB_USER:***REMOVED***} "order_status")
    private OrderStatus status;

    @Column(name ${DB_USER:***REMOVED***} "total_amount", nullable ${DB_USER:***REMOVED***} false)
    private BigInteger totalAmount;

    @Column(name ${DB_USER:***REMOVED***} "created_at", nullable ${DB_USER:***REMOVED***} false, updatable ${DB_USER:***REMOVED***} false)
    private LocalDateTime createdAt;

    @Column(name ${DB_USER:***REMOVED***} "paid_at")
    private LocalDateTime paidAt;

    @Column(name ${DB_USER:***REMOVED***} "updated_at", nullable ${DB_USER:***REMOVED***} false)
    private LocalDateTime updatedAt;

    @PrePersist
    protected void onCreate() {
        createdAt ${DB_USER:***REMOVED***} LocalDateTime.now();
        updatedAt ${DB_USER:***REMOVED***} LocalDateTime.now();
    }

    @PreUpdate
    protected void onUpdate() {
        updatedAt ${DB_USER:***REMOVED***} LocalDateTime.now();
    }
}
