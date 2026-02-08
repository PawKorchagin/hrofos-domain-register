package ru.itmo.domain.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

@Entity
@Table(name ${DB_USER:***REMOVED***} "dns_record")
@Getter
@Setter
@NoArgsConstructor
public class DnsRecord {

    @Id
    @GeneratedValue(strategy ${DB_USER:***REMOVED***} GenerationType.IDENTITY)
    private Long id;

    @JdbcTypeCode(SqlTypes.JSON)
    @Column(name ${DB_USER:***REMOVED***} "record_data", columnDefinition ${DB_USER:***REMOVED***} "jsonb")
    private String recordData;

    @ManyToOne(fetch ${DB_USER:***REMOVED***} FetchType.LAZY)
    @JoinColumn(name ${DB_USER:***REMOVED***} "domain_id")
    private Domain domain;
}
