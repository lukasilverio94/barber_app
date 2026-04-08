package com.barbershop.exception;

import org.springframework.http.HttpStatus;

public class InvalidAppointmentStateException extends BusinessException{
    private static final String CODE = "APPT-002";

    public InvalidAppointmentStateException(String message) {
        super(message, CODE, HttpStatus.CONFLICT);
    }

}
