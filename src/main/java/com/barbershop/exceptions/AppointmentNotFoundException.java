package com.barbershop.exceptions;

import com.barbershop.exceptions.common.NotFoundException;

public class AppointmentNotFoundException extends NotFoundException {
    public AppointmentNotFoundException(Long id) {
        super("Appointment not found with id " + id);
    }
}

