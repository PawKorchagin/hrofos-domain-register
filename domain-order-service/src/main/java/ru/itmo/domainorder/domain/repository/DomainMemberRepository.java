package ru.itmo.domainorder.domain.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.itmo.domainorder.domain.entity.DomainMember;

import java.util.UUID;

@Repository
public interface DomainMemberRepository extends JpaRepository<DomainMember, UUID> {
    boolean existsByDomainIdAndUserId(UUID domainId, UUID userId);
}
