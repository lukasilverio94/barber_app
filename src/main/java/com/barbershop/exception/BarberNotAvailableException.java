package com.barbershop.exception;

import org.springframework.http.HttpStatus;

public class BarberNotAvailableException extends BusinessException {
    private static final String CODE = "BARB-002";

    public BarberNotAvailableException(String message) {
        super(message, CODE, HttpStatus.CONFLICT);
    }
}
