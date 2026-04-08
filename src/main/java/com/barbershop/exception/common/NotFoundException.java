package com.barbershop.exception.common;

import com.barbershop.exception.BusinessException;
import org.springframework.http.HttpStatus;

public abstract class NotFoundException extends BusinessException {
    public NotFoundException(String message, String code) {
        super(message, code, HttpStatus.NOT_FOUND);
    }
}
