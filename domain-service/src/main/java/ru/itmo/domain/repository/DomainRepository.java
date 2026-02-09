package ru.itmo.domain.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.itmo.domain.entity.Domain;

import java.util.List;
import java.util.Optional;

public interface DomainRepository extends JpaRepository<Domain, Long> {

    boolean existsByDomainPartAndParentIsNull(String domainPart);

    Optional<Domain> findByDomainPartAndParentIsNull(String domainPart);

    List<Domain> findAllByParentIsNull();

    List<Domain> findByParentId(Long parentId);

    Optional<Domain> findByParentIdAndDomainPart(Long parentId, String domainPart);
}
