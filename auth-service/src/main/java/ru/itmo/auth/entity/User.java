package ru.itmo.auth.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name ${DB_USER:***REMOVED***} "app_user")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class User {
    @Id
    @GeneratedValue(strategy ${DB_USER:***REMOVED***} GenerationType.AUTO)
    @Column(columnDefinition ${DB_USER:***REMOVED***} "UUID")
    private UUID id;

    @Column(nullable ${DB_USER:***REMOVED***} false, unique ${DB_USER:***REMOVED***} true, length ${DB_USER:***REMOVED***} 255)
    private String email;

    @Column(name ${DB_USER:***REMOVED***} "password_hash", nullable ${DB_USER:***REMOVED***} false, length ${DB_USER:***REMOVED***} 255)
    private String passwordHash;

    @Column(name ${DB_USER:***REMOVED***} "email_verified", nullable ${DB_USER:***REMOVED***} false)
    private Boolean emailVerified ${DB_USER:***REMOVED***} false;

    @Column(name ${DB_USER:***REMOVED***} "verification_token", columnDefinition ${DB_USER:***REMOVED***} "UUID")
    private UUID verificationToken;

    @Column(name ${DB_USER:***REMOVED***} "is_admin", nullable ${DB_USER:***REMOVED***} false)
    private Boolean isAdmin ${DB_USER:***REMOVED***} false;

    @Column(name ${DB_USER:***REMOVED***} "created_at", nullable ${DB_USER:***REMOVED***} false, updatable ${DB_USER:***REMOVED***} false)
    private LocalDateTime createdAt;

    @Column(name ${DB_USER:***REMOVED***} "updated_at", nullable ${DB_USER:***REMOVED***} false)
    private LocalDateTime updatedAt;

    @PrePersist
    protected void onCreate() {
        createdAt ${DB_USER:***REMOVED***} LocalDateTime.now();
        updatedAt ${DB_USER:***REMOVED***} LocalDateTime.now();
        if (verificationToken ${DB_USER:***REMOVED***}${DB_USER:***REMOVED***} null) {
            verificationToken ${DB_USER:***REMOVED***} UUID.randomUUID();
        }
    }

    @PreUpdate
    protected void onUpdate() {
        updatedAt ${DB_USER:***REMOVED***} LocalDateTime.now();
    }
}
