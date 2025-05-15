package com.barbershop.dto;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.UUID;

public record TimeslotDTO(
        UUID barberId,
        LocalDate day,
        LocalTime startTime,
        LocalTime endTime
) {
}
