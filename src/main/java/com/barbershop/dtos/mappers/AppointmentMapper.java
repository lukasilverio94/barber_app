package com.barbershop.dtos.mappers;

import com.barbershop.dtos.AppointmentCreateDTO;
import com.barbershop.dtos.AppointmentDTO;
import com.barbershop.models.Appointment;
import com.barbershop.models.AppointmentStatus;
import com.barbershop.models.Barber;
import com.barbershop.models.Client;

public class AppointmentMapper {

    public static AppointmentDTO toDto(Appointment appointment) {
        return new AppointmentDTO(
                appointment.getId(),
                appointment.getBarber().getName(),
                appointment.getClient().getName(),
                appointment.getDateTime(),
                appointment.getServiceType(),
                appointment.getStatus().name()
        );
    }

    public static Appointment fromCreateDTO(AppointmentCreateDTO dto, Barber barber, Client client) {
        Appointment appointment = new Appointment();
        appointment.setBarber(barber);
        appointment.setClient(client);
        appointment.setDateTime(dto.dateTime());
        appointment.setServiceType(dto.serviceType());
        appointment.setStatus(AppointmentStatus.REQUESTED);
        return appointment;
    }

}
