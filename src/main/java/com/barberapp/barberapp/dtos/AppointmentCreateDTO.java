package com.barberapp.barberapp.dtos;

import java.time.LocalDateTime;

public record AppointmentCreateDTO(
        Long barberId,
        Long clientId,
        LocalDateTime dateTime,
        String serviceType
) {
}
