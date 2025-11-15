package com.travel.tripservice.application.controllers;

import com.travel.tripservice.application.dto.ReservationResponse;
import com.travel.tripservice.application.services.ReservationService;
import com.travel.tripservice.domain.entities.Reservation;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
@Tag(name = "Reservations", description = "Gestión de reservas")
@SecurityRequirement(name = "bearer-jwt")
public class ReservationController {

    private final ReservationService reservationService;

    @PostMapping("/trips/{tripId}/reservations")
    @ResponseStatus(HttpStatus.CREATED)
    @PreAuthorize("hasRole('PASSENGER')")
    @Operation(summary = "Crear una reserva (inicia el Saga)")
    public ReservationResponse createReservation(
            @PathVariable Long tripId,
            @AuthenticationPrincipal Jwt jwt) {
        String passengerId = jwt.getSubject();
        Reservation reservation = reservationService.createReservation(tripId, passengerId);
        return mapToResponse(reservation);
    }

    @GetMapping("/reservations/{id}")
    @PreAuthorize("hasAnyRole('DRIVER', 'PASSENGER')")
    @Operation(summary = "Obtener detalles de una reserva")
    public ReservationResponse getReservation(@PathVariable Long id) {
        Reservation reservation = reservationService.getReservation(id);
        return mapToResponse(reservation);
    }

    private ReservationResponse mapToResponse(Reservation reservation) {
        return ReservationResponse.builder()
                .id(reservation.getId())
                .tripId(reservation.getTripId())
                .passengerId(reservation.getPassengerId())
                .status(reservation.getStatus())
                .createdAt(reservation.getCreatedAt())
                .confirmedAt(reservation.getConfirmedAt())
                .cancelledAt(reservation.getCancelledAt())
                .cancellationReason(reservation.getCancellationReason())
                .build();
    }
}