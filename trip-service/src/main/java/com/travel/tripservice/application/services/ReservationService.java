package com.travel.tripservice.application.services;

import com.travel.tripservice.domain.entities.Reservation;
import com.travel.tripservice.domain.exceptions.ReservationNotFoundException;
import com.travel.tripservice.domain.repositories.ReservationRepository;
import com.travel.tripservice.infrastructure.producers.ReservationProducer;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Servicio principal para la lógica de negocio de las reservas.
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class ReservationService {

    private final ReservationRepository reservationRepository;
    private final ReservationProducer reservationProducer;

    /**
     * Crea una nueva reserva en estado PENDING e inicia el Saga de Microservicios
     * publicando un evento a Kafka.
     *
     * @param tripId ID del viaje para el que se reserva.
     * @param passengerId ID del pasajero (sub de Keycloak).
     * @return La reserva persistida.
     */
    @Transactional
    public Reservation createReservation(Long tripId, String passengerId) {
        log.info("Creando reserva para TripId: {} por PassengerId: {}", tripId, passengerId);

        // 1. Crear la entidad Reservation
        Reservation reservation = Reservation.builder()
                .tripId(tripId)
                .passengerId(passengerId)
                // El estado PENDING y createdAt se establecen automáticamente en @PrePersist
                .build();

        // 2. Persistir la reserva
        Reservation savedReservation = reservationRepository.save(reservation);
        log.info("Reserva {} creada exitosamente.", savedReservation.getId());

        // 3. Iniciar el Saga (Publicar evento a Kafka)
        // El Saga se encargará de verificar la disponibilidad del asiento y la reserva de pago.
        reservationProducer.sendReservationCreatedEvent(savedReservation);

        return savedReservation;
    }

    /**
     * Obtiene una reserva por su ID.
     *
     * @param id ID de la reserva.
     * @return La entidad Reservation.
     * @throws ReservationNotFoundException si la reserva no existe.
     */
    @Transactional(readOnly = true)
    public Reservation getReservation(Long id) {
        log.debug("Buscando reserva con ID: {}", id);
        return reservationRepository.findById(id)
                .orElseThrow(() -> new ReservationNotFoundException(id));
    }

    // Aquí irían otros métodos como:
    // - public void confirmReservation(Long id)
    // - public void cancelReservation(Long id, String reason)
}