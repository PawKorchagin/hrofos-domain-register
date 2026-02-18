package ru.itmo.domain.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import ru.itmo.domain.entity.DomainReservation;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface DomainReservationRepository extends JpaRepository<DomainReservation, Long> {
    
    List<DomainReservation> findByPaymentId(UUID paymentId);
    
    Optional<DomainReservation> findByPaymentIdAndL3Domain(UUID paymentId, String l3Domain);
    
    @Query("SELECT r FROM DomainReservation r WHERE r.l3Domain ${DB_USER:***REMOVED***} :l3Domain AND r.expiresAt > :now")
    List<DomainReservation> findActiveReservationsByL3Domain(@Param("l3Domain") String l3Domain, @Param("now") LocalDateTime now);
    
    @Query("SELECT r FROM DomainReservation r WHERE r.expiresAt <${DB_USER:***REMOVED***} :now")
    List<DomainReservation> findExpiredReservations(@Param("now") LocalDateTime now);
    
    @Modifying
    @Query("DELETE FROM DomainReservation r WHERE r.paymentId ${DB_USER:***REMOVED***} :paymentId")
    void deleteByPaymentId(@Param("paymentId") UUID paymentId);
    
    @Modifying
    @Query("DELETE FROM DomainReservation r WHERE r.expiresAt <${DB_USER:***REMOVED***} :now")
    void deleteExpiredReservations(@Param("now") LocalDateTime now);
}
