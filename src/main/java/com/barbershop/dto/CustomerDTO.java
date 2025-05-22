package com.barbershop.dto;

import java.util.UUID;

public record CustomerDTO(
        UUID id,
        String name,
        String phone,
        String email
) {
}
