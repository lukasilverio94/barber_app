package com.barbershop.exception;

import com.barbershop.exception.common.NotFoundException;

import java.util.UUID;

public class BarberNotFoundException extends NotFoundException {
    public BarberNotFoundException(UUID id) {
        super("Barber not found with id " + id);
    }
}
