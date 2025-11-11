package com.travel.passengerservice.domain.entities;

import jakarta.persistence.*;
import java.time.LocalDateTime;

// Quitamos las anotaciones @Getter y @Setter de Lombok
@Entity
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
    private Double ratingAvg;

    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
    }

    // --- MÉTODOS GETTERS Y SETTERS GENERADOS MANUALMENTE ---

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getKeycloakSub() {
        return keycloakSub;
    }

    public void setKeycloakSub(String keycloakSub) {
        this.keycloakSub = keycloakSub;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public Double getRatingAvg() {
        return ratingAvg;
    }

    public void setRatingAvg(Double ratingAvg) {
        this.ratingAvg = ratingAvg;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }
}