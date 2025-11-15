package com.travel.tripservice.domain.entities;

import com.travel.tripservice.domain.enums.TripStatus;
import jakarta.persistence.*;
import lombok.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "trips")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Trip {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String driverId; // Keycloak sub

    @Column(nullable = false)
    private String origin;

    @Column(nullable = false)
    private String destination;

    @Column(nullable = false)
    private LocalDateTime startTime;

    @Column(nullable = false)
    private Integer seatsTotal;

    @Column(nullable = false)
    private Integer seatsAvailable;

    @Column(nullable = false)
    private BigDecimal price;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private TripStatus status;

    @Column(nullable = false)
    private LocalDateTime createdAt;

    @Column(nullable = false)
    private LocalDateTime updatedAt;

    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
        updatedAt = LocalDateTime.now();
        if (status == null) {
            status = TripStatus.SCHEDULED;
        }
    }

    @PreUpdate
    protected void onUpdate() {
        updatedAt = LocalDateTime.now();
    }

    public boolean hasAvailableSeats() {
        return seatsAvailable > 0 && status == TripStatus.SCHEDULED;
    }

    public void reserveSeat() {
        if (!hasAvailableSeats()) {
            throw new IllegalStateException("No hay asientos disponibles");
        }
        seatsAvailable--;
    }

    public void releaseSeat() {
        if (seatsAvailable >= seatsTotal) {
            throw new IllegalStateException("No se puede liberar más asientos");
        }
        seatsAvailable++;
    }
}