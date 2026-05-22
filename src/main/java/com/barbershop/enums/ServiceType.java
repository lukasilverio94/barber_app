package com.barbershop.enums;

import lombok.Getter;

@Getter
public enum ServiceType {
    HAIRCUT("Cabelo"),
    BEARD("Barba");

    private final String portugueseDescription;

    ServiceType(String portugueseDescription) {
        this.portugueseDescription = portugueseDescription;
    }

}
