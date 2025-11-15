package com.travel.tripservice.application.dto;

import jakarta.validation.constraints.*;
import lombok.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CreateTripRequest {

    @NotBlank(message = "El origen es requerido")
    private String origin;

    @NotBlank(message = "El destino es requerido")
    private String destination;

    @NotNull(message = "La hora de inicio es requerida")
    @Future(message = "La hora debe ser futura")
    private LocalDateTime startTime;

    @NotNull(message = "El número de asientos es requerido")
    @Min(value = 1, message = "Debe haber al menos 1 asiento")
    @Max(value = 8, message = "Máximo 8 asientos")
    private Integer seatsTotal;

    @NotNull(message = "El precio es requerido")
    @DecimalMin(value = "0.0", inclusive = false, message = "El precio debe ser mayor a 0")
    private BigDecimal price;
}