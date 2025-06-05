package com.barbershop.validation;


import com.barbershop.exception.BarberNotAvailableException;
import com.barbershop.repository.AppointmentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.UUID;

@Component
public class AppointmentValidator {

    private final AppointmentRepository appointmentRepository;

    private static final LocalTime OPENING_TIME = LocalTime.of(8, 0);
    private static final LocalTime CLOSING_TIME = LocalTime.of(20, 0);
    private static final int APPOINTMENT_DURATION_IN_MINUTES = 30;

    public AppointmentValidator(AppointmentRepository appointmentRepository) {
        this.appointmentRepository = appointmentRepository;
    }

    public void validateTimeWithinBusinessHours(LocalTime time, LocalDate date) {
        if (time.isBefore(OPENING_TIME) || time.plusMinutes(APPOINTMENT_DURATION_IN_MINUTES).isAfter(CLOSING_TIME)) {
            throw new IllegalArgumentException("Appointment time is outside of business hours.");
        }

        if (date.getDayOfWeek() == DayOfWeek.SUNDAY) {
            throw new IllegalArgumentException("Barbershop is closed on Sundays");
        }

    }

    public void validateBarberAvailability(UUID barberId, LocalDate date, LocalTime time) {
        boolean isOverlapping = appointmentRepository.existsByBarberIdAndApptDayAndTimeRange(
                barberId, date, time, time.plusMinutes(30)
        );

        if (isOverlapping) {
            throw new BarberNotAvailableException("Barber not available at this time. Try again");
        }
    }

}
