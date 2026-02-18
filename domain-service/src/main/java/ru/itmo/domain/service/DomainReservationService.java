package ru.itmo.domain.service;

import java.util.List;
import java.util.UUID;

public interface DomainReservationService {
    
    List<String> reserveDomains(UUID paymentId, UUID userId, List<String> l3Domains, String period, int ttlMinutes);
    
    void confirmReservation(UUID paymentId);
    
    void cancelReservation(UUID paymentId);
    
    long cleanupExpiredReservations();
    
    boolean isDomainReserved(String l3Domain);
}
