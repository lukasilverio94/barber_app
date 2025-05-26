package com.barbershop.exception;

import com.barbershop.exception.common.NotFoundException;

import java.util.UUID;

public class AppointmentNotFoundException extends NotFoundException {
    public AppointmentNotFoundException(UUID id) {
        super("Appointment not found with id " + id);
    }
}

