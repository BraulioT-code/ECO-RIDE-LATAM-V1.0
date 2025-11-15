package com.travel.tripservice.application.dto;

import com.travel.tripservice.domain.enums.ReservationStatus;
import lombok.*;
import java.time.LocalDateTime;

@Data
@Builder
public class ReservationResponse {
    private Long id;
    private Long tripId;
    private String passengerId;
    private ReservationStatus status;
    private LocalDateTime createdAt;
    private LocalDateTime confirmedAt;
    private LocalDateTime cancelledAt;
    private String cancellationReason;
}