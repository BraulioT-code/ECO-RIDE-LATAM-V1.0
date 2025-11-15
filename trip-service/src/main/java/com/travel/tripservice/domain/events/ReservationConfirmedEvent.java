package com.travel.tripservice.domain.events;

import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ReservationConfirmedEvent {
    private Long reservationId;
    private Long tripId;
    private String passengerId;
    private String traceId;
}