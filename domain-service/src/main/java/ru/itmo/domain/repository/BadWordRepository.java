package ru.itmo.domain.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.itmo.domain.entity.BadWord;

import java.util.Optional;

public interface BadWordRepository extends JpaRepository<BadWord, Long> {

    Optional<BadWord> findByWordIgnoreCase(String word);

    boolean existsByWordIgnoreCase(String word);
}
