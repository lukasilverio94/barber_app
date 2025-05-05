package com.barbershop.dtos;

import java.time.LocalTime;

public record TimeRangeDTO(
        LocalTime startTime,
        LocalTime endTime
) {
}
