package com.barbershop.dto;

import com.barbershop.enums.AppointmentStatus;
import com.barbershop.enums.ServiceType;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.UUID;

public record AppointmentResponseDTO(
        UUID id,
        LocalDate day,
        LocalTime startTime,
        LocalTime endTime,
        ServiceType serviceType,
        AppointmentStatus status,
        UUID barberId,
        String customerName
) {}
