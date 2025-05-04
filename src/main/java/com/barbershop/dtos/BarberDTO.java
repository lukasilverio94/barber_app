package com.barbershop.dtos;

import java.util.List;

public record BarberDTO(
        Long id,
        String name,
        String contactInfo,
        String workingHours,
        List<Long> appointmentIds
) {
}
