package ru.itmo.domainorder.dns.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;
import ru.itmo.domainorder.dns.enumeration.DomainRecordType;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name ${DB_USER:***REMOVED***} "dns_record")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class DnsRecord {
    @Id
    @GeneratedValue(strategy ${DB_USER:***REMOVED***} GenerationType.UUID)
    private UUID id;

    @Column(name ${DB_USER:***REMOVED***} "domain_id", nullable ${DB_USER:***REMOVED***} false)
    private UUID domainId;

    @Enumerated(EnumType.STRING)
    @JdbcTypeCode(SqlTypes.NAMED_ENUM)
    @Column(nullable ${DB_USER:***REMOVED***} false, columnDefinition ${DB_USER:***REMOVED***} "domain_record_type")
    private DomainRecordType type;

    @Column(nullable ${DB_USER:***REMOVED***} false, length ${DB_USER:***REMOVED***} 255)
    private String name;

    @Column(nullable ${DB_USER:***REMOVED***} false, length ${DB_USER:***REMOVED***} 1024)
    private String value;

    @Column(nullable ${DB_USER:***REMOVED***} false)
    private Integer ttl;

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
