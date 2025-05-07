package com.barbershop.exception;

import com.barbershop.exception.common.NotFoundException;

public class BarberNotFoundException extends NotFoundException {
    public BarberNotFoundException(Long id) {
        super("Barber not found with id " + id);
    }
}
