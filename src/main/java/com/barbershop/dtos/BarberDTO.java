package com.barbershop.dtos;

import java.time.DayOfWeek;
import java.util.List;
import java.util.Map;

public record BarberDTO(
        Long id,
        String name,
        String contactInfo,
        Map<DayOfWeek, TimeRangeDTO> availableDays,
        List<Long> appointmentIds,
        List<String> servicesOffered
) {
}
