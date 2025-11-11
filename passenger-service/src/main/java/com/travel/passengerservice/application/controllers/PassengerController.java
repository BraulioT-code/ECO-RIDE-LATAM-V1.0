package com.travel.passengerservice.application.controllers;

import com.travel.passengerservice.domain.entities.Passenger;
import com.travel.passengerservice.infraestructure.repositories.PassengerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.Optional;

@RestController
@RequestMapping("/me")
public class PassengerController {

    @Autowired
    private PassengerRepository passengerRepository;

    /**
     * Recupera el perfil del usuario actual usando el Keycloak Subject (sub).
     * El 'Principal' es el objeto de seguridad que Spring Security inyecta.
     * [cite: 127]
     */
    @GetMapping
    public ResponseEntity<Passenger> getMyProfile(Principal principal) {
        // En un Resource Server (como este), el 'principal.getName()' es el Subject (Keycloak Sub).
        String keycloakSub = principal.getName();

        Optional<Passenger> passenger = passengerRepository.findByKeycloakSub(keycloakSub);

        if (passenger.isEmpty()) {
            // Si no existe, lo creamos (esto es un flujo de "auto-registro" simplificado)
            Passenger newPassenger = new Passenger();
            newPassenger.setKeycloakSub(keycloakSub);
            newPassenger.setName("Usuario ID: " + keycloakSub.substring(0, 5));
            newPassenger.setEmail("user-" + keycloakSub.substring(0, 5) + "@ecoride.com");
            return ResponseEntity.status(HttpStatus.CREATED).body(passengerRepository.save(newPassenger));
        }

        return ResponseEntity.ok(passenger.get());
    }
}