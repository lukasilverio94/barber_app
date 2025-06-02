package com.barbershop.dto;

import java.util.Set;
import java.util.UUID;

public record BarberDTO(
        UUID id,
        String name,
        String phone,
        Set<String> serviceType
) { }
