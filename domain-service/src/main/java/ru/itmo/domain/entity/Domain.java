package ru.itmo.domain.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Entity
@Table(name ${DB_USER:***REMOVED***} "domain")
@Getter
@Setter
@NoArgsConstructor
public class Domain {

    @Id
    @GeneratedValue(strategy ${DB_USER:***REMOVED***} GenerationType.IDENTITY)
    private Long id;

    @Column(name ${DB_USER:***REMOVED***} "domain_part", nullable ${DB_USER:***REMOVED***} false)
    private String domainPart;

    @Column(name ${DB_USER:***REMOVED***} "domain_version")
    private Long domainVersion;

    @Column(name ${DB_USER:***REMOVED***} "user_id")
    private UUID userId;

    @ManyToOne(fetch ${DB_USER:***REMOVED***} FetchType.LAZY)
    @JoinColumn(name ${DB_USER:***REMOVED***} "parent_id")
    private Domain parent;

    @OneToMany(mappedBy ${DB_USER:***REMOVED***} "parent", cascade ${DB_USER:***REMOVED***} CascadeType.ALL, orphanRemoval ${DB_USER:***REMOVED***} true)
    private List<Domain> children ${DB_USER:***REMOVED***} new ArrayList<>();

    @OneToMany(mappedBy ${DB_USER:***REMOVED***} "domain", cascade ${DB_USER:***REMOVED***} CascadeType.ALL, orphanRemoval ${DB_USER:***REMOVED***} true)
    private List<DnsRecord> dnsRecords ${DB_USER:***REMOVED***} new ArrayList<>();
}
