package com.travel.tripservice.application.services;

import com.travel.tripservice.application.dto.CreateTripRequest;
import com.travel.tripservice.application.dto.TripResponse;
import com.travel.tripservice.domain.entities.Trip;
import com.travel.tripservice.domain.enums.TripStatus;
import com.travel.tripservice.domain.repositories.TripRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class TripService {

    private final TripRepository tripRepository;

    @Transactional
    public TripResponse createTrip(CreateTripRequest request, String driverId) {
        log.info("Creando viaje para conductor {}", driverId);

        Trip trip = Trip.builder()
                .driverId(driverId)
                .origin(request.getOrigin())
                .destination(request.getDestination())
                .startTime(request.getStartTime())
                .seatsTotal(request.getSeatsTotal())
                .seatsAvailable(request.getSeatsTotal())
                .price(request.getPrice())
                .status(TripStatus.SCHEDULED)
                .build();

        trip = tripRepository.save(trip);
        log.info("Viaje {} creado exitosamente", trip.getId());

        return mapToResponse(trip);
    }

    public List<TripResponse> searchTrips(String origin, String destination,
                                          LocalDateTime from, LocalDateTime to) {
        log.info("Buscando viajes: origin={}, destination={}, from={}, to={}",
                origin, destination, from, to);

        List<Trip> trips = tripRepository.findAvailableTrips(
                origin, destination, from, to, TripStatus.SCHEDULED);

        return trips.stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    public TripResponse getTripById(Long id) {
        Trip trip = tripRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Viaje no encontrado"));
        return mapToResponse(trip);
    }

    public List<TripResponse> getMyTrips(String driverId) {
        return tripRepository.findByDriverId(driverId).stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    private TripResponse mapToResponse(Trip trip) {
        return TripResponse.builder()
                .id(trip.getId())
                .driverId(trip.getDriverId())
                .origin(trip.getOrigin())
                .destination(trip.getDestination())
                .startTime(trip.getStartTime())
                .seatsTotal(trip.getSeatsTotal())
                .seatsAvailable(trip.getSeatsAvailable())
                .price(trip.getPrice())
                .status(trip.getStatus())
                .createdAt(trip.getCreatedAt())
                .build();
    }
}