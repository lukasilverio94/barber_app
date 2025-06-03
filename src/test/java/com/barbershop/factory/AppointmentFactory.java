package com.barbershop.factory;

import com.barbershop.enums.AppointmentStatus;
import com.barbershop.enums.ServiceType;
import com.barbershop.model.Appointment;
import com.barbershop.model.Barber;
import com.barbershop.model.Customer;

import java.time.LocalDate;
import java.time.LocalTime;

public class AppointmentFactory {

    public static Appointment createDefaultAppointment(Barber barber, Customer customer) {
        Appointment appointment = new Appointment();
        appointment.setApptDay(LocalDate.of(2025, 6, 15));
        appointment.setStartTime(LocalTime.of(15, 30));
        appointment.setEndTime(LocalTime.of(16, 0));
        appointment.setServiceType(ServiceType.HAIRCUT);
        appointment.setStatus(AppointmentStatus.REQUESTED);
        appointment.setBarber(barber);
        appointment.setCustomer(customer);
        return appointment;
    }

    public static Appointment createAppointmentWithTime(
            Barber barber,
            Customer customer,
            LocalDate date,
            LocalTime startTime,
            LocalTime endTime
    ) {
        Appointment appointment = new Appointment();
        appointment.setApptDay(date);
        appointment.setStartTime(startTime);
        appointment.setEndTime(endTime);
        appointment.setServiceType(ServiceType.HAIRCUT);
        appointment.setStatus(AppointmentStatus.REQUESTED);
        appointment.setBarber(barber);
        appointment.setCustomer(customer);
        return appointment;
    }
}
