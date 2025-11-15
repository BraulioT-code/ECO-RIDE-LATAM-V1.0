package com.travel.tripservice.domain.entities;

import com.travel.tripservice.domain.enums.ReservationStatus;
import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "reservations")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Reservation {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private Long tripId;

    @Column(nullable = false)
    private String passengerId; // Keycloak sub

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private ReservationStatus status;

    @Column(nullable = false)
    private LocalDateTime createdAt;

    @Column
    private LocalDateTime confirmedAt;

    @Column
    private LocalDateTime cancelledAt;

    @Column
    private String cancellationReason;

    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
        if (status == null) {
            status = ReservationStatus.PENDING;
        }
    }

    public void confirm() {
        if (status != ReservationStatus.PENDING) {
            throw new IllegalStateException("Solo se pueden confirmar reservas pendientes");
        }
        status = ReservationStatus.CONFIRMED;
        confirmedAt = LocalDateTime.now();
    }

    public void cancel(String reason) {
        if (status == ReservationStatus.CANCELLED) {
            throw new IllegalStateException("La reserva ya está cancelada");
        }
        status = ReservationStatus.CANCELLED;
        cancelledAt = LocalDateTime.now();
        cancellationReason = reason;
    }
}