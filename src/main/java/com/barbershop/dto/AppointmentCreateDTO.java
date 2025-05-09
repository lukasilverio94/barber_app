package com.barbershop.dto;

public record AppointmentCreateDTO(
        Long barberId,
        Long customerId,
        String date,
        String time,
        String serviceType
) {
}
