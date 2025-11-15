package com.travel.tripservice.domain.events;

import lombok.*;
import java.math.BigDecimal;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ReservationRequestedEvent {
    private Long reservationId;
    private Long tripId;
    private String passengerId;
    private BigDecimal amount;
    private String traceId;
}