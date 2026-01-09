package ru.itmo.auth.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name ${DB_USER:***REMOVED***} "refresh_token")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class RefreshToken {
    @Id
    @GeneratedValue(strategy ${DB_USER:***REMOVED***} GenerationType.AUTO)
    @Column(columnDefinition ${DB_USER:***REMOVED***} "UUID")
    private UUID id;

    @Column(name ${DB_USER:***REMOVED***} "user_id", nullable ${DB_USER:***REMOVED***} false)
    private UUID userId;

    @Column(nullable ${DB_USER:***REMOVED***} false, unique ${DB_USER:***REMOVED***} true, length ${DB_USER:***REMOVED***} 512)
    private String token;

    @Column(name ${DB_USER:***REMOVED***} "expires_at", nullable ${DB_USER:***REMOVED***} false)
    private LocalDateTime expiresAt;

    @Column(name ${DB_USER:***REMOVED***} "created_at", nullable ${DB_USER:***REMOVED***} false, updatable ${DB_USER:***REMOVED***} false)
    private LocalDateTime createdAt;

    @PrePersist
    protected void onCreate() {
        createdAt ${DB_USER:***REMOVED***} LocalDateTime.now();
    }
}
