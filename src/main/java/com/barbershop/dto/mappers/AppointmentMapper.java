package com.barbershop.dto.mappers;

import com.barbershop.dto.AppointmentCreateDTO;
import com.barbershop.dto.AppointmentResponseDTO;
import com.barbershop.enums.AppointmentStatus;
import com.barbershop.model.Appointment;
import com.barbershop.model.Barber;
import com.barbershop.model.Customer;

public class AppointmentMapper {

    public static AppointmentResponseDTO toDto(Appointment appointment) {
        if (appointment == null) {
            return null;
        }

        assert appointment.getBarber() != null;
        return new AppointmentResponseDTO(
                appointment.getId(),
                appointment.getApptDay(),
                appointment.getStartTime(),
                appointment.getEndTime(),
                appointment.getServiceType(),
                appointment.getStatus(),
                appointment.getBarber().getId(),
                appointment.getCustomer().getName(),
                appointment.getCustomer().getPhone()
        );
    }

    public static Appointment fromCreateDto(AppointmentCreateDTO dto, Barber barber, Customer customer) {
        if (dto == null) return null;

        Appointment appointment = new Appointment();
        appointment.setBarber(barber);
        appointment.setCustomer(customer);
        appointment.setApptDay(dto.date());
        appointment.setStartTime(dto.startTime());
        appointment.setEndTime(dto.startTime().plusMinutes(30));
        appointment.setServiceType(dto.serviceType());
        appointment.setStatus(AppointmentStatus.REQUESTED);

        return appointment;
    }



}