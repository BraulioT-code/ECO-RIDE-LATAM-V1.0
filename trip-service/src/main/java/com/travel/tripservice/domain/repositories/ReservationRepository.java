package com.travel.tripservice.domain.repositories;

import com.travel.tripservice.domain.entities.Reservation;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ReservationRepository extends JpaRepository<Reservation, Long> {
    // Para consultas específicas de la Saga
    Reservation findByTripIdAndPassengerId(Long tripId, Long passengerId);
}