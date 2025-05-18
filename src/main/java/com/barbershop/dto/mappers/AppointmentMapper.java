package com.barbershop.dto.mappers;

import com.barbershop.dto.AppointmentCreateDTO;
import com.barbershop.dto.AppointmentDTO;
import com.barbershop.model.Appointment;
import com.barbershop.enums.AppointmentStatus;
import com.barbershop.model.Barber;
import com.barbershop.model.Customer;
import com.barbershop.model.Timeslot;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.UUID;

public class AppointmentMapper {

    public static AppointmentDTO toDto(Appointment appointment) {
        if (appointment == null) {
            return null;
        }

        UUID barberId = appointment.getBarber() != null ? appointment.getBarber().getId() : null;
        UUID customerId = appointment.getCustomer() != null ? appointment.getCustomer().getId() : null;

        return new AppointmentDTO(
                appointment.getId(),
                appointment.getDay(),
                appointment.getStartTime(),
                appointment.getEndTime(),
                appointment.getServiceType(),
                appointment.getStatus(),
                barberId,
                customerId
        );
    }

    public static Appointment fromCreateDto(AppointmentCreateDTO dto, Barber barber, Customer customer, Timeslot timeslot) {
        if (dto == null) {
            return null;
        }

        LocalDate date = LocalDate.parse(dto.date());
        LocalTime time = LocalTime.parse(dto.time());

        Appointment appointment = new Appointment();
        appointment.setBarber(barber);
        appointment.setCustomer(customer);
        appointment.setTimeslot(timeslot);
        appointment.setDay(date);
        appointment.setStartTime(time);
        appointment.setEndTime(time.plusHours(1)); // Assuming 1 hour appointments
        appointment.setServiceType(dto.serviceType());
        appointment.setStatus(AppointmentStatus.REQUESTED);

        return appointment;
    }
}