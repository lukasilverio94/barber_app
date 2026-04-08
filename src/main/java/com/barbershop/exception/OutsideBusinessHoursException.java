package com.barbershop.exception;

import org.springframework.http.HttpStatus;

import java.time.LocalDate;
import java.time.LocalTime;

public class OutsideBusinessHoursException extends BusinessException {
    private static final String CODE = "SCH-001";

    public OutsideBusinessHoursException(LocalTime time, LocalDate date) {
        super("Time " + time + " on " + date + " is outside business hours",
                CODE, HttpStatus.BAD_REQUEST);
    }
}