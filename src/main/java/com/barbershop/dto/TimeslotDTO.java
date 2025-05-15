package com.barbershop.dto;

import com.barbershop.enums.Availability;
import com.barbershop.model.Barber;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.UUID;

public record TimeslotDTO(
        UUID id,
        Availability availability,
        LocalDate day,
        LocalTime startTime,
        LocalTime endTime,
        Barber barber
) {
}
