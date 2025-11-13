package com.travel.tripservice.domain.entities;

import jakarta.persistence.*;
import lombok.Data;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "trip")
@Data // Incluye @Getter, @Setter, @ToString, @EqualsAndHashCode
public class Trip {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // ID del conductor (Driver) obtenido de Keycloak (SUB)
    @Column(name = "driver_id", nullable = false)
    private String driverId;

    private String origin;
    private String destination;

    @Column(name = "start_time", nullable = false)
    private LocalDateTime startTime;

    @Column(name = "seats_total", nullable = false)
    private Integer seatsTotal;

    @Column(name = "seats_available", nullable = false)
    private Integer seatsAvailable; // Se decrementa al hacer una reserva

    private BigDecimal price; // Monto simbólico por trayecto

    @Enumerated(EnumType.STRING)
    private TripStatus status;
}