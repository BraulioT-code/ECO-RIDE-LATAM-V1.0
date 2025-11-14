package com.travel.tripservice.domain.enums;

public enum ReservationStatus {
    PENDING,        // Saga en curso
    CONFIRMED,      // Pago autorizado
    CANCELLED       // Pago fallido o compensación aplicada
}