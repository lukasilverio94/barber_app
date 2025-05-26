package com.barbershop.dto.mappers;

import com.barbershop.dto.AppointmentCreateDTO;
import com.barbershop.dto.AppointmentResponseDTO;
import com.barbershop.enums.AppointmentStatus;
import com.barbershop.model.Appointment;
import com.barbershop.model.Barber;
import com.barbershop.model.Customer;
import com.barbershop.model.Timeslot;

import java.time.LocalDate;
import java.time.LocalTime;

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
                appointment.getTimeslot().getEndTime(),
                appointment.getServiceType(),
                appointment.getStatus(),
                appointment.getBarber().getId(),
                appointment.getCustomer().getName(),
                appointment.getCustomer().getPhone()
        );
    }

    public static Appointment fromCreateDto(AppointmentCreateDTO dto, Barber barber, Customer customer, Timeslot timeslot) {
        if (dto == null) {
            return null;
        }

        LocalDate date = dto.date();
        LocalTime startTime = dto.startTime();

        Appointment appointment = new Appointment();
        appointment.setBarber(barber);
        appointment.setCustomer(customer);
        appointment.setTimeslot(timeslot);
        appointment.setApptDay(date);
        appointment.setStartTime(startTime);
        appointment.setEndTime(startTime.plusMinutes(30));
        appointment.setServiceType(dto.serviceType());
        appointment.setStatus(AppointmentStatus.REQUESTED);

        return appointment;
    }
}