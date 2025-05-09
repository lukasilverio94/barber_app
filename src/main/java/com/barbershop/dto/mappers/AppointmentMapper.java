package com.barbershop.dto.mappers;

import com.barbershop.dto.AppointmentCreateDTO;
import com.barbershop.dto.AppointmentDTO;
import com.barbershop.model.Appointment;
import com.barbershop.model.AppointmentStatus;
import com.barbershop.model.Barber;
import com.barbershop.model.Customer;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class AppointmentMapper {

    public static AppointmentDTO toDto(Appointment appointment) {
        DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        DateTimeFormatter timeFormatter = DateTimeFormatter.ofPattern("HH:mm:ss");

        String formattedDate = appointment.getDateTime().format(dateFormatter);
        String formattedTime = appointment.getDateTime().format(timeFormatter);

        return new AppointmentDTO(
                appointment.getId(),
                appointment.getBarber().getName(),
                appointment.getCustomer().getName(),
                appointment.getServiceType(),
                formattedDate,
                formattedTime,
                appointment.getStatus().name()
        );
    }

    public static Appointment fromCreateDTO(AppointmentCreateDTO dto, Barber barber, Customer customer) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");
        String dateAndTimeCombined = dto.date() + " " + dto.time();
        LocalDateTime dateTime = LocalDateTime.parse(dateAndTimeCombined, formatter);

        Appointment appointment = new Appointment();
        appointment.setBarber(barber);
        appointment.setCustomer(customer);
        appointment.setDateTime(dateTime);
        appointment.setServiceType(dto.serviceType());
        appointment.setStatus(AppointmentStatus.REQUESTED);
        return appointment;
    }

}
