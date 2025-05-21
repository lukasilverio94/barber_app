package com.barbershop.dto;

import com.barbershop.enums.ServiceType;
import com.fasterxml.jackson.annotation.JsonFormat;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.UUID;

public record AppointmentCreateDTO(
        UUID barberId,
        UUID customerId,
        @JsonFormat(pattern = "dd/MM/yyyy")
        LocalDate date,
        @JsonFormat(pattern = "HH:mm")
        LocalTime startTime,
        ServiceType serviceType
) {
}
