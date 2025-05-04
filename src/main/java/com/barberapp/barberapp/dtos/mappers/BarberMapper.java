package com.barberapp.barberapp.dtos.mappers;

import com.barberapp.barberapp.dtos.BarberDTO;
import com.barberapp.barberapp.models.Barber;
import com.barberapp.barberapp.models.Appointment;

import java.util.List;
import java.util.stream.Collectors;

public class BarberMapper {

    public static BarberDTO toDTO(Barber barber) {
        List<Long> appointmentIds = barber.getAppointments() != null
                ? barber.getAppointments().stream()
                .map(Appointment::getId)
                .collect(Collectors.toList())
                : List.of();

        return new BarberDTO(
                barber.getId(),
                barber.getName(),
                barber.getContactInfo(),
                barber.getWorkingHours(),
                appointmentIds
        );
    }

    // Optional: if you ever need to convert from DTO to entity
    public static Barber toEntity(BarberDTO dto) {
        Barber barber = new Barber();
        barber.setId(dto.id());
        barber.setName(dto.name());
        barber.setContactInfo(dto.contactInfo());
        barber.setWorkingHours(dto.workingHours());
        // appointments not set here (requires full Appointment objects)
        return barber;
    }
}
