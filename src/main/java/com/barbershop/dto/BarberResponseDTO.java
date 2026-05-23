package com.barbershop.dto;

import com.barbershop.enums.ServiceType;

import java.util.Set;
import java.util.UUID;

public record BarberResponseDTO(
        UUID id,
        String name,
        String phone,
        String email,
        Set<ServiceType> serviceType
) {
}