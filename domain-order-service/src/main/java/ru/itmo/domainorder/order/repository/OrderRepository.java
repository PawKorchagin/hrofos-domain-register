package ru.itmo.domainorder.order.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import ru.itmo.domainorder.order.entity.Order;
import ru.itmo.domainorder.order.enumeration.OrderStatus;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Repository
public interface OrderRepository extends JpaRepository<Order, UUID> {
    Page<Order> findByUserId(UUID userId, Pageable pageable);
    
    @Query("SELECT o FROM Order o WHERE o.userId ${DB_USER:***REMOVED***} :userId AND o.status ${DB_USER:***REMOVED***} :status")
    Page<Order> findByUserIdAndStatus(@Param("userId") UUID userId, 
                                      @Param("status") OrderStatus status, 
                                      Pageable pageable);
    
    @Query("SELECT o FROM Order o WHERE o.paidAt IS NULL AND o.createdAt < :date")
    List<Order> findUnpaidOrdersOlderThan(@Param("date") LocalDateTime date);
}
