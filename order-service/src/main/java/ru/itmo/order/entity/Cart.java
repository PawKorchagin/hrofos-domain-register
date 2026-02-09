package ru.itmo.order.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.IdClass;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.UUID;

@Entity
@Table(name ${DB_USER:***REMOVED***} "cart")
@IdClass(CartId.class)
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Cart {

    @Id
    @Column(name ${DB_USER:***REMOVED***} "user_id", nullable ${DB_USER:***REMOVED***} false)
    private UUID userId;

    @Id
    @Column(name ${DB_USER:***REMOVED***} "l3_domain", nullable ${DB_USER:***REMOVED***} false, length ${DB_USER:***REMOVED***} 255)
    private String l3Domain;
}
