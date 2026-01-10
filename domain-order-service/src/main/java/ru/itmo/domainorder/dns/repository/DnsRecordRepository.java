package ru.itmo.domainorder.dns.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.itmo.domainorder.dns.entity.DnsRecord;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface DnsRecordRepository extends JpaRepository<DnsRecord, UUID> {
    List<DnsRecord> findByDomainId(UUID domainId);
    Optional<DnsRecord> findByIdAndDomainId(UUID id, UUID domainId);
    void deleteByDomainId(UUID domainId);
}
