package consumer;

import entities.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;
import repositories.PaymentIntentRepository;

import java.time.LocalDateTime;
import java.util.Random;

@Slf4j
@Component
@RequiredArgsConstructor
public class PaymentConsumer {

    private final PaymentIntentRepository paymentRepo;
    private final KafkaTemplate<String, Object> kafkaTemplate;

    @KafkaListener(topics = "reservation-requested", groupId = "payment-service")
    public void handleReservationRequested(ReservationRequestedEvent event) {
        log.info("🔔 ReservationRequested received for reservationId={} amount={}",
                event.getReservationId(), event.getAmount());

        // Crear PaymentIntent
        PaymentIntent intent = PaymentIntent.builder()
                .reservationId(event.getReservationId())
                .amount(event.getAmount())
                .currency("USD")
                .status(PaymentStatus.REQUIRES_ACTION)
                .createdAt(LocalDateTime.now())
                .build();

        paymentRepo.save(intent);

        // Simulación de resultado de pago
        boolean success = new Random().nextBoolean();

        if (success) {
            intent.setStatus(PaymentStatus.AUTHORIZED);
            paymentRepo.save(intent);

            PaymentAuthorizedEvent authorizedEvent = new PaymentAuthorizedEvent(
                    event.getReservationId(), intent.getId(), "AUTHORIZED"
            );
            kafkaTemplate.send("payment-authorized", authorizedEvent);
            log.info("✅ PaymentAuthorized emitted for reservationId={}", event.getReservationId());
        } else {
            intent.setStatus(PaymentStatus.FAILED);
            paymentRepo.save(intent);

            PaymentFailedEvent failedEvent = new PaymentFailedEvent(
                    event.getReservationId(), "Insufficient funds"
            );
            kafkaTemplate.send("payment-failed", failedEvent);
            log.warn("❌ PaymentFailed emitted for reservationId={}", event.getReservationId());
        }
    }
}