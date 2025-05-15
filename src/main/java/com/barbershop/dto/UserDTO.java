package com.barbershop.dto;

import java.util.UUID;

public record UserDTO(
        UUID id,
        String name,
        String phone,
        String type
) {}
