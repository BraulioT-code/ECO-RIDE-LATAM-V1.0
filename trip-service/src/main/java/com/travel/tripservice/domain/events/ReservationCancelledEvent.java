package com.travel.tripservice.domain.events;

import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ReservationCancelledEvent {
    private Long reservationId;
    private Long tripId;
    private String passengerId;
    private String reason;
    private String traceId;
}