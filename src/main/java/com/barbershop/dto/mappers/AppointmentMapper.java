package com.barbershop.dto.mappers;

import com.barbershop.dto.AppointmentCreateDTO;
import com.barbershop.dto.AppointmentDTO;
import com.barbershop.model.Appointment;
import com.barbershop.model.AppointmentStatus;
import com.barbershop.model.Barber;
import com.barbershop.model.Customer;

public class AppointmentMapper {

    public static AppointmentDTO toDto(Appointment appointment) {
        return new AppointmentDTO(
                appointment.getId(),
                appointment.getBarber().getName(),
                appointment.getCustomer().getName(),
                appointment.getDateTime(),
                appointment.getServiceType(),
                appointment.getStatus().name()
        );
    }

    public static Appointment fromCreateDTO(AppointmentCreateDTO dto, Barber barber, Customer customer) {
        Appointment appointment = new Appointment();
        appointment.setBarber(barber);
        appointment.setCustomer(customer);
        appointment.setDateTime(dto.dateTime());
        appointment.setServiceType(dto.serviceType());
        appointment.setStatus(AppointmentStatus.REQUESTED);
        return appointment;
    }

}
