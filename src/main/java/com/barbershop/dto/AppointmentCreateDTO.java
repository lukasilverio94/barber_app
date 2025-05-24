package com.barbershop.dto;

import com.barbershop.enums.ServiceType;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.UUID;

public record AppointmentCreateDTO(
        UUID barberId,
        UUID customerId,
        LocalDate date,
        LocalTime startTime,
        ServiceType serviceType
) {
}
