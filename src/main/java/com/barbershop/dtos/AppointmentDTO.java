package com.barbershop.dtos;

import java.time.LocalDateTime;

public record AppointmentDTO(
        Long id,
        String barberName,
        String clientName,
        LocalDateTime dateTime,
        String serviceType,
        String status
) {}