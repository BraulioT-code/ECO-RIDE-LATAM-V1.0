package com.travel.passengerservice.infraestructure.repositories;

import com.travel.passengerservice.domain.entities.Passenger;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface PassengerRepository extends JpaRepository<Passenger, Long> {
    // Método para buscar un pasajero por el ID de Keycloak
    Optional<Passenger> findByKeycloakSub(String keycloakSub);
}