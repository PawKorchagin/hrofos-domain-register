package ru.itmo.auth.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.itmo.auth.entity.AuthFactor;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface AuthFactorRepository extends JpaRepository<AuthFactor, UUID> {
    List<AuthFactor> findByUserId(UUID userId);
    
    Optional<AuthFactor> findByUserIdAndKind(UUID userId, AuthFactor.AuthFactorKind kind);
    
    boolean existsByUserIdAndKind(UUID userId, AuthFactor.AuthFactorKind kind);
    
    void deleteByUserIdAndKind(UUID userId, AuthFactor.AuthFactorKind kind);
}
