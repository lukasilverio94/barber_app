package com.barbershop.dto.mappers;

import com.barbershop.dto.AppointmentCreateDTO;
import com.barbershop.dto.AppointmentDTO;
import com.barbershop.model.Appointment;
import com.barbershop.enums.AppointmentStatus;
import com.barbershop.model.Barber;
import com.barbershop.model.Customer;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class AppointmentMapper {

    public static AppointmentDTO toDto(Appointment appointment) {
        return new AppointmentDTO(
                null,
                appointment.getDay(),
                appointment.getStartTime(),
                appointment.getEndTime(),
                appointment.getServiceType(),
                appointment.getStatus(),
                appointment.getBarber().getId(),
                appointment.getCustomer().getId()
        );
    }

    public static Appointment fromCreateDTO(AppointmentCreateDTO dto, Barber barber, Customer customer) {
        Appointment appointment = new Appointment();
        appointment.setBarber(barber);
        appointment.setCustomer(customer);
        appointment.setServiceType(dto.serviceType());
        appointment.setStatus(AppointmentStatus.REQUESTED);
        return appointment;
    }

}
