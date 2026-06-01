package com.barbershop.exception;

import org.springframework.http.HttpStatus;

import java.time.LocalDate;
import java.time.LocalTime;

public class BarberNotWorkingException extends BusinessException {
    private static final String CODE = "BARB-003";

    public BarberNotWorkingException(LocalDate date, LocalTime time) {
        super(
                String.format(
                        "Barber is not working on %s at %s",
                        date,
                        time
                ),
                CODE,
                HttpStatus.BAD_REQUEST
        );
    }
}
