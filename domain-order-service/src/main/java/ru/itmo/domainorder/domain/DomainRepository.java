package ru.itmo.domainorder.domain;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface DomainRepository extends JpaRepository<Domain, UUID> {
    boolean existsByZone2Id(UUID zone2Id);
    long countByZone2Id(UUID zone2Id);
}
