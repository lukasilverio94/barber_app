package com.barbershop.dto;

import java.time.LocalTime;

public record TimeRangeDTO(
        LocalTime startTime,
        LocalTime endTime
) {
}
