package com.travel.tripservice.domain.events;

import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PaymentFailedEvent {
    private Long reservationId;
    private String reason;
    private String traceId;
}