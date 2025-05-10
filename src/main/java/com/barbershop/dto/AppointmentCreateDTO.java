package com.barbershop.dto;

import com.barbershop.enums.ServiceType;

public record AppointmentCreateDTO(
        Long barberId,
        Long customerId,
        String date,
        String time,
        ServiceType serviceType
) {
}
