package com.barbershop.dto;

import com.barbershop.enums.ServiceType;
import jakarta.validation.constraints.FutureOrPresent;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.UUID;

public record AppointmentCreateDTO(
        @NotNull UUID barberId,
        @NotNull UUID customerId,
        @NotNull @FutureOrPresent LocalDate date,
        @NotNull LocalTime startTime,
        @NotNull  ServiceType serviceType
) {
}
