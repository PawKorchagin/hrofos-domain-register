package ru.itmo.domainorder.domain;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name ${DB_USER:***REMOVED***} "domain")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Domain {
    @Id
    @GeneratedValue(strategy ${DB_USER:***REMOVED***} GenerationType.UUID)
    private UUID id;

    @Column(nullable ${DB_USER:***REMOVED***} false, unique ${DB_USER:***REMOVED***} true, length ${DB_USER:***REMOVED***} 63)
    private String fqdn;

    @Column(name ${DB_USER:***REMOVED***} "zone2_id", nullable ${DB_USER:***REMOVED***} false)
    private UUID zone2Id;

    @Column(name ${DB_USER:***REMOVED***} "activated_at")
    private LocalDateTime activatedAt;

    @Column(name ${DB_USER:***REMOVED***} "expires_at", nullable ${DB_USER:***REMOVED***} false)
    private LocalDateTime expiresAt;

    @Column(name ${DB_USER:***REMOVED***} "created_at", nullable ${DB_USER:***REMOVED***} false, updatable ${DB_USER:***REMOVED***} false)
    private LocalDateTime createdAt;

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
