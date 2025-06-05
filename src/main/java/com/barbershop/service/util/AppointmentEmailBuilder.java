package com.barbershop.service.util;

import com.barbershop.model.Barber;
import com.barbershop.model.Customer;
import com.barbershop.model.Email;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;

public class AppointmentEmailBuilder {

    private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("MMMM d, yyyy");
    private static final DateTimeFormatter TIME_FORMATTER = DateTimeFormatter.ofPattern("hh:mm a");

    public static Email buildConfirmationEmail(Customer customer, Barber barber, LocalDate date, LocalTime time) {
        String subject = "Appointment Confirmation";
        String body = String.format(
                "Hello %s,\n\nYour appointment with %s is confirmed for %s at %s.\n\nThank you!",
                customer.getName(), barber.getName(),
                date.format(DATE_FORMATTER), time.format(TIME_FORMATTER)
        );
        return new Email(customer.getEmail(), subject, body);
    }

    public static Email buildCancellationEmail(Customer customer, Barber barber, LocalDate date, LocalTime time) {
        String subject = "Appointment Cancellation";
        String body = String.format(
                "Hello %s,\n\nWe regret to inform you that your appointment with %s on %s at %s has been cancelled.\n\nPlease reschedule at your convenience.\n\nSorry for the inconvenience.",
                customer.getName(), barber.getName(),
                date.format(DATE_FORMATTER), time.format(TIME_FORMATTER)
        );
        return new Email(customer.getEmail(), subject, body);
    }

}
