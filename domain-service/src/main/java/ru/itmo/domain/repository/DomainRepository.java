package ru.itmo.domain.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import ru.itmo.domain.entity.Domain;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface DomainRepository extends JpaRepository<Domain, Long> {

    boolean existsByDomainPartAndParentIsNull(String domainPart);

    Optional<Domain> findByDomainPartAndParentIsNull(String domainPart);

    List<Domain> findAllByParentIsNull();

    List<Domain> findByParentId(Long parentId);

    Optional<Domain> findByParentIdAndDomainPart(Long parentId, String domainPart);

    boolean existsByParentIdAndDomainPart(Long parentId, String domainPart);

    List<Domain> findByUserIdAndParentIsNotNull(UUID userId);

    List<Domain> findByParentIsNotNullAndFinishedAtBefore(java.time.LocalDateTime now);

    @Query("SELECT COUNT(DISTINCT d.userId) FROM Domain d WHERE d.userId IS NOT NULL")
    long countDistinctUserIds();

    @Query("SELECT COUNT(d) FROM Domain d WHERE d.parent IS NOT NULL")
    long countRegisteredDomains();
}
