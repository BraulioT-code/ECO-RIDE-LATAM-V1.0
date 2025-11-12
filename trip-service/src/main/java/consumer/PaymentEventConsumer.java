package consumer;

import entities.PaymentAuthorizedEvent;
import entities.PaymentFailedEvent;
import entities.Reservation;
import entities.ReservationStatus;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;
import repositories.ReservationRepository;

import java.util.Optional;

@Slf4j
@Component
@RequiredArgsConstructor
public class PaymentEventConsumer {

    private final ReservationRepository reservationRepository;

    @KafkaListener(topics = "payment-authorized", groupId = "trip-service")
    public void handlePaymentAuthorized(PaymentAuthorizedEvent event) {
        log.info("✅ PaymentAuthorized received for reservationId={}", event.getReservationId());

        Optional<Reservation> optReservation = reservationRepository.findById(event.getReservationId());
        if (optReservation.isEmpty()) {
            log.warn("Reservation not found: {}", event.getReservationId());
            return;
        }

        Reservation reservation = optReservation.get();
        reservation.setStatus(ReservationStatus.CONFIRMED);
        reservationRepository.save(reservation);

        log.info("🟢 Reservation {} marked as CONFIRMED", reservation.getId());
    }

    @KafkaListener(topics = "payment-failed", groupId = "trip-service")
    public void handlePaymentFailed(PaymentFailedEvent event) {
        log.info("❌ PaymentFailed received for reservationId={}, reason={}", event.getReservationId(), event.getReason());

        Optional<Reservation> optReservation = reservationRepository.findById(event.getReservationId());
        if (optReservation.isEmpty()) {
            log.warn("Reservation not found: {}", event.getReservationId());
            return;
        }

        Reservation reservation = optReservation.get();
        reservation.setStatus(ReservationStatus.CANCELLED);
        reservationRepository.save(reservation);

        log.info("🔴 Reservation {} marked as CANCELLED", reservation.getId());
    }
}
