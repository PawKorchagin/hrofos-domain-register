package ru.itmo.domain.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.itmo.domain.entity.DnsRecord;

import java.util.List;

public interface DnsRecordRepository extends JpaRepository<DnsRecord, Long> {

    List<DnsRecord> findByDomainId(Long domainId);
}
