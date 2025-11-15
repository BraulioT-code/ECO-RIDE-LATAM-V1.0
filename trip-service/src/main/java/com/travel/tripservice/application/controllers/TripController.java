package com.travel.tripservice.application.controllers;

import com.travel.tripservice.application.dto.CreateTripRequest;
import com.travel.tripservice.application.dto.TripResponse;
import com.travel.tripservice.application.services.TripService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.*;
import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/api/trips")
@RequiredArgsConstructor
@Tag(name = "Trips", description = "Gestión de viajes")
@SecurityRequirement(name = "bearer-jwt")
public class TripController {

    private final TripService tripService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    @PreAuthorize("hasRole('DRIVER')")
    @Operation(summary = "Crear un nuevo viaje (solo conductores)")
    public TripResponse createTrip(
            @Valid @RequestBody CreateTripRequest request,
            @AuthenticationPrincipal Jwt jwt) {
        String driverId = jwt.getSubject();
        return tripService.createTrip(request, driverId);
    }

    @GetMapping
    @PreAuthorize("hasAnyRole('DRIVER', 'PASSENGER')")
    @Operation(summary = "Buscar viajes disponibles")
    public List<TripResponse> searchTrips(
            @RequestParam(required = false) String origin,
            @RequestParam(required = false) String destination,
            @RequestParam(required = false)
            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime from,
            @RequestParam(required = false)
            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime to) {
        return tripService.searchTrips(origin, destination, from, to);
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasAnyRole('DRIVER', 'PASSENGER')")
    @Operation(summary = "Obtener detalles de un viaje")
    public TripResponse getTrip(@PathVariable Long id) {
        return tripService.getTripById(id);
    }

    @GetMapping("/my-trips")
    @PreAuthorize("hasRole('DRIVER')")
    @Operation(summary = "Obtener mis viajes como conductor")
    public List<TripResponse> getMyTrips(@AuthenticationPrincipal Jwt jwt) {
        String driverId = jwt.getSubject();
        return tripService.getMyTrips(driverId);
    }
}