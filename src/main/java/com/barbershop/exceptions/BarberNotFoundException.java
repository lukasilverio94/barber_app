package com.barbershop.exceptions;

import com.barbershop.exceptions.common.NotFoundException;

public class BarberNotFoundException extends NotFoundException {
    public BarberNotFoundException(Long id) {
        super("Barber not found with id " + id);
    }
}
