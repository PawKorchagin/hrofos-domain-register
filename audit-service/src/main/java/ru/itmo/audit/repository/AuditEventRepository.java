package ru.itmo.audit.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.itmo.audit.entity.AuditEvent;

import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.UUID;

public interface AuditEventRepository extends JpaRepository<AuditEvent, Long> {
    List<AuditEvent> findByUserIdOrderByEventTimeDesc(UUID userId);
    List<AuditEvent> findAllByOrderByEventTimeDesc(Pageable pageable);
}
