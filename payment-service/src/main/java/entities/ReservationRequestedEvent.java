package entities;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ReservationRequestedEvent {
    private Long reservationId;
    private Long tripId;
    private String passengerId;
    private Double amount;
}