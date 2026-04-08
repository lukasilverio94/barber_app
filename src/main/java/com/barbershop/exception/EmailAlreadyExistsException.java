package com.barbershop.exception;

import org.springframework.http.HttpStatus;

public class EmailAlreadyExistsException extends BusinessException {
    private static final String CODE = "CUST-002";

    public EmailAlreadyExistsException(String email) {
        super("Email already registered: " + email, CODE, HttpStatus.CONFLICT);
    }
}

