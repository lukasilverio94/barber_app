package com.barbershop.enums;

public enum ServiceType {
    HAIRCUT("Cabelo"),
    BEARD("Barba");

    private final String portugueseDescription;

    ServiceType(String portugueseDescription) {
        this.portugueseDescription = portugueseDescription;
    }

    public String getPortugueseDescription() {
        return portugueseDescription;
    }
}
