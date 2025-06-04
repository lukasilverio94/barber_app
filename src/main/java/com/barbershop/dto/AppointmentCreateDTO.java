package com.barbershop.dto;

import com.barbershop.enums.ServiceType;
import jakarta.validation.constraints.FutureOrPresent;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.UUID;

public record AppointmentCreateDTO(
        @NotNull(message = "Appointment date is required") UUID barberId,
        @NotNull(message = "Customer ID is required") UUID customerId,
        @NotNull(message = "Date is required")
        @FutureOrPresent(message = "Date must be today or in the future") LocalDate date,
        @NotNull LocalTime startTime,
        @NotNull  ServiceType serviceType
) {
}
