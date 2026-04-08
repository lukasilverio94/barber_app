package com.barbershop.exception;

import org.springframework.http.HttpStatus;

public class ExpiredResetTokenException extends BusinessException {
    private static final String CODE = "AUTH-002";

    public ExpiredResetTokenException() {
        super("Reset token has expired", CODE, HttpStatus.BAD_REQUEST);
    }
}