package com.barbershop.exception;

import org.springframework.http.HttpStatus;

public class InvalidResetTokenException extends BusinessException {
    private static final String CODE = "AUTH-001";

    public InvalidResetTokenException() {
        super("Invalid or expired reset token", CODE, HttpStatus.BAD_REQUEST);
    }
}