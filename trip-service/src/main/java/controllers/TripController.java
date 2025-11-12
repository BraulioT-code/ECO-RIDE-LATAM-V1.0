package controllers;

import entities.*;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import repositories.ReservationRepository;
import repositories.TripRepository;

import java.time.LocalDateTime;
import java.util.Optional;

@RestController
@RequestMapping("/trips")
@RequiredArgsConstructor
public class TripController {

    private final TripRepository tripRepository;
    private final ReservationRepository reservationRepository;
    private final KafkaTemplate<String, Object> kafkaTemplate;
    private static final String TOPIC = "reservation-requested";

    @PreAuthorize("hasRole('DRIVER')")
    @PostMapping
    public ResponseEntity<Trip> createTrip(@RequestBody Trip trip) {
        trip.setStatus(TripStatus.ACTIVE);
        trip.setSeatsAvailable(trip.getSeatsTotal());
        Trip saved = tripRepository.save(trip);
        return ResponseEntity.ok(saved);
    }

    @PreAuthorize("hasRole('PASSENGER')")
    @PostMapping("/{tripId}/reservations")
    public ResponseEntity<String> reserveSeat(@PathVariable Long tripId, @RequestParam String passengerId) {
        Optional<Trip> tripOpt = tripRepository.findById(tripId);
        if (tripOpt.isEmpty()) return ResponseEntity.notFound().build();

        Trip trip = tripOpt.get();
        if (trip.getSeatsAvailable() <= 0)
            return ResponseEntity.badRequest().body("No seats available");

        Reservation reservation = Reservation.builder()
                .trip(trip)
                .passengerId(passengerId)
                .status(ReservationStatus.PENDING)
                .createdAt(LocalDateTime.now())
                .build();

        reservationRepository.save(reservation);

        // Emitir evento Kafka
        ReservationRequestedEvent event = new ReservationRequestedEvent(
                reservation.getId(),
                trip.getId(),
                passengerId,
                trip.getPrice()
        );

        kafkaTemplate.send(TOPIC, event);
        trip.setSeatsAvailable(trip.getSeatsAvailable() - 1);
        tripRepository.save(trip);

        return ResponseEntity.ok("Reservation requested successfully");
    }
}