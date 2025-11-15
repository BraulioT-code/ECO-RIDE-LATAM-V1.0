package com.travel.tripservice.domain.repositories;

import com.travel.tripservice.domain.entities.Trip;
import com.travel.tripservice.domain.enums.TripStatus;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Arrays;
import java.util.List;
import java.time.LocalDateTime;

public interface TripRepository extends JpaRepository<Trip, Long> {
    // Para el listado con filtros
    List<Trip> findByOriginAndDestinationAndStartTimeBetween(
            String origin, String destination, LocalDateTime from, LocalDateTime to);

    List<Trip> findByDriverId(String driverId);

    List<Trip> findAvailableTrips(String origin, String destination, LocalDateTime from, LocalDateTime to, TripStatus tripStatus);
}

