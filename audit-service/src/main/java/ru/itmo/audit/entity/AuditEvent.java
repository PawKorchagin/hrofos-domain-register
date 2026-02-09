package ru.itmo.audit.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name ${DB_USER:***REMOVED***} "audit_event")
@Getter
@Setter
@NoArgsConstructor
public class AuditEvent {

    @Id
    @GeneratedValue(strategy ${DB_USER:***REMOVED***} GenerationType.IDENTITY)
    private Long id;

    @Column(name ${DB_USER:***REMOVED***} "description", nullable ${DB_USER:***REMOVED***} false)
    private String description;

    @Column(name ${DB_USER:***REMOVED***} "user_id")
    private UUID userId;

    @Column(name ${DB_USER:***REMOVED***} "event_time", nullable ${DB_USER:***REMOVED***} false)
    private LocalDateTime eventTime;

    @PrePersist
    protected void onCreate() {
        if (eventTime ${DB_USER:***REMOVED***}${DB_USER:***REMOVED***} null) {
            eventTime ${DB_USER:***REMOVED***} LocalDateTime.now();
        }
    }
}
