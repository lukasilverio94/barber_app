package com.barbershop.dto;

import com.barbershop.enums.ServiceType;

public record AppointmentDTO(
        Long id,
        String barberName,
        String clientName,
        ServiceType serviceType,
        String date,
        String time,
        String status
) {}