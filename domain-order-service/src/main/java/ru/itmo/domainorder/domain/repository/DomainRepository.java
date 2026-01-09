package ru.itmo.domainorder.domain.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import ru.itmo.domainorder.domain.entity.Domain;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface DomainRepository extends JpaRepository<Domain, UUID> {
    boolean existsByZone2Id(UUID zone2Id);
    long countByZone2Id(UUID zone2Id);
    boolean existsByFqdn(String fqdn);
    Optional<Domain> findByFqdn(String fqdn);
    
    @Query("SELECT d FROM Domain d WHERE d.zone2Id ${DB_USER:***REMOVED***} :zoneId")
    Page<Domain> findByZoneId(@Param("zoneId") UUID zoneId, Pageable pageable);
    
    @Query("SELECT d FROM Domain d WHERE d.expiresAt < :date")
    List<Domain> findExpiredDomains(@Param("date") LocalDateTime date);
    
    @Query("SELECT d FROM Domain d WHERE d.expiresAt BETWEEN :startDate AND :endDate")
    List<Domain> findDomainsExpiringBetween(@Param("startDate") LocalDateTime startDate, 
                                            @Param("endDate") LocalDateTime endDate);
}
