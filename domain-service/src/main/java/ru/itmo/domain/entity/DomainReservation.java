package ru.itmo.domain.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name ${DB_USER:***REMOVED***} "domain_reservation", uniqueConstraints ${DB_USER:***REMOVED***} {
    @UniqueConstraint(columnNames ${DB_USER:***REMOVED***} {"payment_id", "l3_domain"})
})
@Getter
@Setter
@NoArgsConstructor
public class DomainReservation {

    @Id
    @GeneratedValue(strategy ${DB_USER:***REMOVED***} GenerationType.IDENTITY)
    private Long id;

    @Column(name ${DB_USER:***REMOVED***} "payment_id", nullable ${DB_USER:***REMOVED***} false)
    private UUID paymentId;

    @Column(name ${DB_USER:***REMOVED***} "user_id", nullable ${DB_USER:***REMOVED***} false)
    private UUID userId;

    @Column(name ${DB_USER:***REMOVED***} "l3_domain", nullable ${DB_USER:***REMOVED***} false)
    private String l3Domain;

    @Column(name ${DB_USER:***REMOVED***} "period", nullable ${DB_USER:***REMOVED***} false, length ${DB_USER:***REMOVED***} 16)
    private String period;

    @Column(name ${DB_USER:***REMOVED***} "expires_at", nullable ${DB_USER:***REMOVED***} false)
    private LocalDateTime expiresAt;

    @Column(name ${DB_USER:***REMOVED***} "created_at", nullable ${DB_USER:***REMOVED***} false)
    private LocalDateTime createdAt;

    @PrePersist
    protected void onCreate() {
        if (createdAt ${DB_USER:***REMOVED***}${DB_USER:***REMOVED***} null) {
            createdAt ${DB_USER:***REMOVED***} LocalDateTime.now();
        }
    }
}
