package com.travel.tripservice.domain.entities;

public enum ReservationStatus {
    PENDING,        // Estado inicial, esperando la confirmación de pago (Saga en curso)
    CONFIRMED,      // Pago autorizado y reserva asegurada (Saga exitosa)
    CANCELLED       // Pago fallido o compensación aplicada (Saga fallida)
}