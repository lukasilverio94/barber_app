package com.barbershop.exception;

import java.time.LocalDate;
import java.time.LocalTime;

public class OutsideBusinessHoursException extends RuntimeException {
    public OutsideBusinessHoursException(LocalTime time, LocalDate date) {
        super("Appointment time " + time + " on " + date + " is outside business hours.");
    }
}