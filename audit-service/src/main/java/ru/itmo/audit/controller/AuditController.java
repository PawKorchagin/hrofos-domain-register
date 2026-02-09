package ru.itmo.audit.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.itmo.audit.entity.AuditEvent;
import ru.itmo.audit.repository.AuditEventRepository;

import java.util.Map;
import java.util.UUID;

@RestController
@RequestMapping("/audit")
@RequiredArgsConstructor
public class AuditController {

    private final AuditEventRepository auditEventRepository;

    @PostMapping("/events")
    public ResponseEntity<Void> createEvent(@RequestBody Map<String, String> body) {
        String description ${DB_USER:***REMOVED***} body.get("description");
        String userIdStr ${DB_USER:***REMOVED***} body.get("userId");

        if (description ${DB_USER:***REMOVED***}${DB_USER:***REMOVED***} null || description.isBlank()) {
            return ResponseEntity.badRequest().build();
        }

        AuditEvent event ${DB_USER:***REMOVED***} new AuditEvent();
        event.setDescription(description);
        if (userIdStr !${DB_USER:***REMOVED***} null && !userIdStr.isBlank()) {
            try {
                event.setUserId(UUID.fromString(userIdStr));
            } catch (IllegalArgumentException ignored) {
            }
        }
        auditEventRepository.save(event);

        return ResponseEntity.status(HttpStatus.CREATED).build();
    }
}
