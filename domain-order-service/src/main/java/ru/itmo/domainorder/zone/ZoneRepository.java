package ru.itmo.domainorder.zone;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface ZoneRepository extends JpaRepository<Zone, UUID> {
    Optional<Zone> findByName(String name);
    boolean existsByName(String name);
}
