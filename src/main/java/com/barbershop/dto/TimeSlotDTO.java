package com.barbershop.dto;

import java.time.LocalDateTime;
import java.util.UUID;

public record TimeSlotDTO(
        UUID id,
        LocalDateTime startTime,
        LocalDateTime endTime,
        UUID barberId,
        String availability
) {}
