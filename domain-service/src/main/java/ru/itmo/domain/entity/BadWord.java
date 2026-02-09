package ru.itmo.domain.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name ${DB_USER:***REMOVED***} "bad_word")
@Getter
@Setter
@NoArgsConstructor
public class BadWord {

    @Id
    @GeneratedValue(strategy ${DB_USER:***REMOVED***} GenerationType.IDENTITY)
    private Long id;

    @Column(name ${DB_USER:***REMOVED***} "word", nullable ${DB_USER:***REMOVED***} false, unique ${DB_USER:***REMOVED***} true)
    private String word;
}
