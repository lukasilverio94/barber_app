package com.barbershop.dto.mappers;

import com.barbershop.model.Barber;
import com.barbershop.model.TimeRange;
import com.barbershop.model.Appointment;
import com.barbershop.dto.BarberDTO;
import com.barbershop.dto.TimeRangeDTO;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class BarberMapper {

    public static BarberDTO toDTO(Barber barber) {
        return new BarberDTO(
                barber.getId(),
                barber.getName(),
                barber.getPhone(),
                convertTimeRangeMap(barber.getAvailableDays()),
                extractAppointmentIds(barber.getAppointments()),
                barber.getServicesOffered()
        );
    }

    private static Map<java.time.DayOfWeek, TimeRangeDTO> convertTimeRangeMap(Map<java.time.DayOfWeek, TimeRange> original) {
        if (original == null) return null;

        return original.entrySet().stream()
                .collect(Collectors.toMap(
                        Map.Entry::getKey,
                        e -> new TimeRangeDTO(e.getValue().getStartTime(), e.getValue().getEndTime())
                ));
    }

    private static List<Long> extractAppointmentIds(List<Appointment> appointments) {
        if (appointments == null) return null;

        return appointments.stream()
                .map(Appointment::getId)
                .collect(Collectors.toList());
    }
}
