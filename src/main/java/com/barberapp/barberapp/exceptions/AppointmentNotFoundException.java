package com.barberapp.barberapp.exceptions;

import com.barberapp.barberapp.exceptions.common.NotFoundException;

public class AppointmentNotFoundException extends NotFoundException {
    public AppointmentNotFoundException(Long id) {
        super("Appointment not found with id " + id);
    }
}

