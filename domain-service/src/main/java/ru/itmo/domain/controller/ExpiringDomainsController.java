package ru.itmo.domain.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.itmo.domain.entity.Domain;
import ru.itmo.domain.repository.DomainRepository;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.*;

@RestController
@RequestMapping("${openapi.openAPIDNS.base-path:/domains}")
@RequiredArgsConstructor
public class ExpiringDomainsController {

    private final DomainRepository domainRepository;

    @GetMapping("/userDomains/expiring")
    public ResponseEntity<List<Map<String, Object>>> getExpiringDomains(@RequestParam int days) {
        LocalDate targetDate ${DB_USER:***REMOVED***} LocalDate.now().plusDays(days);
        LocalDateTime start ${DB_USER:***REMOVED***} targetDate.atStartOfDay();
        LocalDateTime end ${DB_USER:***REMOVED***} targetDate.plusDays(1).atStartOfDay();

        List<Domain> domains ${DB_USER:***REMOVED***} domainRepository.findByParentIsNotNullAndFinishedAtBetween(start, end);

        List<Map<String, Object>> result ${DB_USER:***REMOVED***} new ArrayList<>();
        for (Domain d : domains) {
            Map<String, Object> entry ${DB_USER:***REMOVED***} new LinkedHashMap<>();
            entry.put("userId", d.getUserId());
            entry.put("domainName", d.getDomainPart() + "." + d.getParent().getDomainPart());
            entry.put("finishedAt", d.getFinishedAt().toString());
            result.add(entry);
        }

        return ResponseEntity.ok(result);
    }
}
