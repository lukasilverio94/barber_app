package com.barbershop.validation;


import com.barbershop.exception.BarberNotAvailableException;
import com.barbershop.exception.BarberNotWorkingException;
import com.barbershop.exception.OutsideBusinessHoursException;
import com.barbershop.repository.AppointmentRepository;
import com.barbershop.service.BarberAvailabilityService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.UUID;

@Component
@Slf4j
public class AppointmentValidator {

    private final AppointmentRepository appointmentRepository;
    private final BarberAvailabilityService availabilityService;

    private static final LocalTime OPENING_TIME = LocalTime.of(8, 0);
    private static final LocalTime CLOSING_TIME = LocalTime.of(20, 0);
    private static final int APPOINTMENT_DURATION_IN_MINUTES = 30;

    public AppointmentValidator(AppointmentRepository appointmentRepository, BarberAvailabilityService availabilityService) {
        this.appointmentRepository = appointmentRepository;
        this.availabilityService = availabilityService;
    }

    public void validateBarberScheduleOrThrow(UUID barberId, LocalDate date, LocalTime time) {
        boolean isWorking = availabilityService.isBarberWorkingAt(barberId, date, time);
        if (!isWorking) {
            log.error("Barber {} is not schedule to work on {} at {}", barberId, date, time);
            throw new BarberNotWorkingException(date, time);
        }
    }

    public void validateAppointmentConflictOrThrow(UUID barberId, LocalDate date, LocalTime time) {
        boolean isOverlapping = appointmentRepository.existsByBarberIdAndApptDayAndTimeRange(
                barberId, date, time, time.plusMinutes(APPOINTMENT_DURATION_IN_MINUTES)
        );

        if (isOverlapping) {
            log.error("Barber {} already has an appointment on {} at {}", barberId, date, time);
            throw new BarberNotAvailableException("Barber not available at this time. Try again");
        }

    }
}
