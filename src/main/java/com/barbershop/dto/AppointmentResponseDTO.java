package com.barbershop.dto;

import com.barbershop.enums.AppointmentStatus;
import com.barbershop.enums.ServiceType;
import com.fasterxml.jackson.annotation.JsonFormat;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.UUID;

public record AppointmentResponseDTO(
        UUID id,
        @JsonFormat(pattern = "dd/MM/yyyy")
        LocalDate day,
        @JsonFormat(pattern = "HH:mm")
        LocalTime startTime,
        @JsonFormat(pattern = "HH:mm")
        LocalTime endTime,
        ServiceType serviceType,
        AppointmentStatus status,
        UUID barberId,
        String customerName
) {}
