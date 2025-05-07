package com.barbershop.exception;

import com.barbershop.exception.common.NotFoundException;

public class AppointmentNotFoundException extends NotFoundException {
    public AppointmentNotFoundException(Long id) {
        super("Appointment not found with id " + id);
    }
}

