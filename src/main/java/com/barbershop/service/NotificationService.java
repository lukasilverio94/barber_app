package com.barbershop.service;

import com.barbershop.model.Barber;
import com.barbershop.model.Customer;
import com.barbershop.service.util.AppointmentEmailBuilder;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalTime;

@Service
public class NotificationService {

    private final EmailService emailService;

    public NotificationService(EmailService emailService) {
        this.emailService = emailService;
    }

    @Async
    public void sendAppointmentConfirmation(Customer customer, Barber barber, LocalDate date, LocalTime time) {
        emailService.sendSimpleEmail(
                AppointmentEmailBuilder.buildConfirmationEmail(customer, barber, date, time)
        );
    }

    @Async
    public void sendAppointmentCancellation(Customer customer, Barber barber, LocalDate date, LocalTime time) {
        emailService.sendSimpleEmail(
                AppointmentEmailBuilder.buildCancellationEmail(customer, barber, date, time)
        );
    }

}
