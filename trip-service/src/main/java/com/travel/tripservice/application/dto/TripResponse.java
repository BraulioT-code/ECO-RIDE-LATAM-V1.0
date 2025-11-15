package com.travel.tripservice.application.dto;

import com.travel.tripservice.domain.enums.TripStatus;
import lombok.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@Builder
public class TripResponse {
    private Long id;
    private String driverId;
    private String origin;
    private String destination;
    private LocalDateTime startTime;
    private Integer seatsTotal;
    private Integer seatsAvailable;
    private BigDecimal price;
    private TripStatus status;
    private LocalDateTime createdAt;
}