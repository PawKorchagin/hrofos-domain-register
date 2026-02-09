package ru.itmo.audit.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.itmo.audit.entity.AuditEvent;

public interface AuditEventRepository extends JpaRepository<AuditEvent, Long> {
}
