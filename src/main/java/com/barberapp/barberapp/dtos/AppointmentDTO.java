package com.barberapp.barberapp.dtos;

import java.time.LocalDateTime;

public record AppointmentDTO(
        Long id,
        String barberName,
        String clientName,
        LocalDateTime dateTime,
        String serviceType,
        String status
) {}