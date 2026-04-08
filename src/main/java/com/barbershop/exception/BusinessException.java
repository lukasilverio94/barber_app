package com.barbershop.exception;

import lombok.Getter;
import lombok.Setter;
import org.springframework.http.HttpStatus;

@Getter
@Setter
public abstract class BusinessException extends RuntimeException {
    private final String code;
    private final HttpStatus status;

    protected BusinessException(String message, String code, HttpStatus status) {
        super(message);
        this.code = code;
        this.status = status;
    }

}
