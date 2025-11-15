package com.travel.tripservice.domain.events;

import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PaymentAuthorizedEvent {
    private Long reservationId;
    private String paymentIntentId;
    private String chargeId;
    private String traceId;
}