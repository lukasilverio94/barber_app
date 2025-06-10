package com.barbershop.dto;

import jakarta.validation.constraints.NotBlank;

public record LoginRequestDTO(
        @NotBlank(message = "Email is required to login")
        String email,

        @NotBlank(message = "Password is required to login")
        String password) {
}
