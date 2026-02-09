package ru.itmo.audit.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import ru.itmo.audit.entity.AuditEvent;
import ru.itmo.audit.repository.AuditEventRepository;

import java.util.*;

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

    @GetMapping("/events/my")
    public ResponseEntity<List<Map<String, Object>>> getMyEvents() {
        Authentication auth ${DB_USER:***REMOVED***} SecurityContextHolder.getContext().getAuthentication();
        if (auth ${DB_USER:***REMOVED***}${DB_USER:***REMOVED***} null || auth.getPrincipal() ${DB_USER:***REMOVED***}${DB_USER:***REMOVED***} null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }

        UUID userId ${DB_USER:***REMOVED***} (UUID) auth.getPrincipal();
        List<AuditEvent> events ${DB_USER:***REMOVED***} auditEventRepository.findByUserIdOrderByEventTimeDesc(userId);

        List<Map<String, Object>> result ${DB_USER:***REMOVED***} new ArrayList<>();
        for (AuditEvent e : events) {
            Map<String, Object> map ${DB_USER:***REMOVED***} new LinkedHashMap<>();
            map.put("id", e.getId());
            map.put("description", e.getDescription());
            map.put("eventTime", e.getEventTime() !${DB_USER:***REMOVED***} null ? e.getEventTime().toString() : null);
            result.add(map);
        }

        return ResponseEntity.ok(result);
    }

    @GetMapping("/events/all")
    public ResponseEntity<List<Map<String, Object>>> getAllEvents(
            @RequestParam(defaultValue ${DB_USER:***REMOVED***} "100") int limit) {
        List<AuditEvent> events ${DB_USER:***REMOVED***} auditEventRepository.findAllByOrderByEventTimeDesc(
                PageRequest.of(0, Math.min(limit, 500)));

        List<Map<String, Object>> result ${DB_USER:***REMOVED***} new ArrayList<>();
        for (AuditEvent e : events) {
            Map<String, Object> map ${DB_USER:***REMOVED***} new LinkedHashMap<>();
            map.put("id", e.getId());
            map.put("description", e.getDescription());
            map.put("userId", e.getUserId() !${DB_USER:***REMOVED***} null ? e.getUserId().toString() : null);
            map.put("eventTime", e.getEventTime() !${DB_USER:***REMOVED***} null ? e.getEventTime().toString() : null);
            result.add(map);
        }

        return ResponseEntity.ok(result);
    }
}
