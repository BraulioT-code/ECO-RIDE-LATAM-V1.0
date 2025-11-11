package com.travel.passengerservice.domain.entities;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@Table(name = "passenger")
public class Passenger {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // keycloak_sub es el ID único del usuario en Keycloak (Subject)
    @Column(name = "keycloak_sub", unique = true, nullable = false)
    private String keycloakSub;

    private String name;
    private String email;

    @Column(name = "rating_avg")
    private Double ratingAvg; // rating_avg del modelo de datos [cite: 109]

    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
    }
}