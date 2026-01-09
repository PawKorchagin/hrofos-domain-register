package ru.itmo.auth.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name ${DB_USER:***REMOVED***} "auth_factor")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class AuthFactor {
    @Id
    @GeneratedValue(strategy ${DB_USER:***REMOVED***} GenerationType.AUTO)
    @Column(columnDefinition ${DB_USER:***REMOVED***} "UUID")
    private UUID id;

    @Column(name ${DB_USER:***REMOVED***} "user_id", nullable ${DB_USER:***REMOVED***} false)
    private UUID userId;

    @Enumerated(EnumType.STRING)
    @JdbcTypeCode(SqlTypes.NAMED_ENUM)
    @Column(nullable ${DB_USER:***REMOVED***} false, columnDefinition ${DB_USER:***REMOVED***} "auth_factor_kind")
    private AuthFactorKind kind;

    @JdbcTypeCode(SqlTypes.JSON)
    @Column(name ${DB_USER:***REMOVED***} "public_data", nullable ${DB_USER:***REMOVED***} false, columnDefinition ${DB_USER:***REMOVED***} "JSONB")
    private String publicData;

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

    public enum AuthFactorKind {
        TOTP,
        WebAuthn
    }
}
