package com.barbershop.dto;

public record AppointmentDTO(
        Long id,
        String barberName,
        String clientName,
        String serviceType,
        String date,
        String time,
        String status
) {}