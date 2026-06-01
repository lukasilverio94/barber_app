package com.barbershop.service;

import com.barbershop.model.BarberAvailability;
import com.barbershop.repository.BarberAvailabilityRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class BarberAvailabilityService {

    private final BarberAvailabilityRepository repository;

    public boolean isBarberWorkingAt(UUID barberId, LocalDate date, LocalTime appointmentTime) {
        DayOfWeek dayOfWeek = date.getDayOfWeek();

        List<BarberAvailability> availabilities = repository.findByBarberIdAndDayOfWeek(barberId, dayOfWeek);

        return availabilities.stream()
                .anyMatch(a -> !appointmentTime.isBefore(a.getStartTime()) &&
                        appointmentTime.isBefore(a.getEndTime()));
    }

}
