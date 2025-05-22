package com.barbershop.dto;

public record CustomerCreateDTO(
        String name,
        String phone,
        String email
) {
}
