package com.barbershop.dto;

import java.util.UUID;

public record TimeslotCreateDTO(
        String date,
        String startTime,
        String endTime,
        UUID barberId
) {}