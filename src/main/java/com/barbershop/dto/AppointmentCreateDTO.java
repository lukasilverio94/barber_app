package com.barbershop.dto;

import java.time.LocalDateTime;

public record AppointmentCreateDTO(
        Long barberId,
        Long customerId,
        LocalDateTime dateTime,
        String serviceType
) {
}
