package com.travel.tripservice.infrastructure.producers;

import com.travel.tripservice.domain.entities.Reservation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

/**
 * Servicio encargado de publicar eventos de Kafka relacionados con la reserva.
 * Esto es parte de la lógica del patrón Saga.
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class ReservationProducer {

    // private final KafkaTemplate<String, Object> kafkaTemplate; // (Descomentar en implementación real)
    private static final String RESERVATION_TOPIC = "reservation-events";

    /**
     * Publica el evento que inicia el Saga de reserva.
     * @param reservation La entidad de reserva recién creada.
     */
    public void sendReservationCreatedEvent(Reservation reservation) {
        log.info("📢 Iniciando Saga: Enviando evento de Reserva Creada para TripId: {} y ReservationId: {}",
                reservation.getTripId(), reservation.getId());

        // En una implementación real, se serializaría y enviaría un objeto de evento a Kafka.
        // Ejemplo de envío:
        // kafkaTemplate.send(RESERVATION_TOPIC, "CREATED", reservation.getId());

        log.debug("Evento de reserva creada simulado enviado correctamente.");
    }
}