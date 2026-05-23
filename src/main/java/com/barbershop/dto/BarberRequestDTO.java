package com.barbershop.dto;

import java.util.Set;

public record BarberRequestDTO(
        String name,
        String phone,
        Set<String> serviceType
) { }
