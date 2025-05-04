package com.barberapp.barberapp.exceptions;

import com.barberapp.barberapp.exceptions.common.NotFoundException;

public class BarberNotFoundException extends NotFoundException {
    public BarberNotFoundException(Long id) {
        super("Barber not found with id " + id);
    }
}
