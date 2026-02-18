package ru.itmo.domain.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.itmo.domain.service.DomainReservationService;

import java.util.List;
import java.util.Map;
import java.util.UUID;

@RestController
@RequestMapping("/domains/reservations")
@RequiredArgsConstructor
public class DomainReservationController {

    private final DomainReservationService reservationService;

    @PostMapping
    public ResponseEntity<List<String>> reserveDomains(@RequestBody ReserveDomainsRequest request) {
        List<String> reserved ${DB_USER:***REMOVED***} reservationService.reserveDomains(
                request.paymentId(),
                request.userId(),
                request.l3Domains(),
                request.period(),
                request.ttlMinutes()
        );
        return ResponseEntity.ok(reserved);
    }

    @PostMapping("/{paymentId}/confirm")
    public ResponseEntity<Void> confirmReservation(@PathVariable UUID paymentId) {
        reservationService.confirmReservation(paymentId);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/{paymentId}")
    public ResponseEntity<Void> cancelReservation(@PathVariable UUID paymentId) {
        reservationService.cancelReservation(paymentId);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/cleanup")
    public ResponseEntity<Map<String, Long>> cleanupExpired() {
        long count ${DB_USER:***REMOVED***} reservationService.cleanupExpiredReservations();
        return ResponseEntity.ok(Map.of("cleaned", count));
    }

    public record ReserveDomainsRequest(
            UUID paymentId,
            UUID userId,
            List<String> l3Domains,
            String period,
            int ttlMinutes
    ) {}
}
