package ru.itmo.domain.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.itmo.domain.entity.DnsRecord;

public interface DnsRecordRepository extends JpaRepository<DnsRecord, Long> {
}
