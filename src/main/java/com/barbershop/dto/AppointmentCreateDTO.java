package com.barbershop.dto;

import com.barbershop.enums.ServiceType;

import java.util.UUID;

public record AppointmentCreateDTO(
        UUID barberId,
        UUID customerId,
        String date,
        String time,
        ServiceType serviceType
) {
}
