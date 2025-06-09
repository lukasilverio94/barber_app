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

        LocalTime endTime = time.plusMinutes(30);

        String googleCalendarLink = CalendarLinkGenerator.generateGoogleCalendarLink(
                "Barbershop Appointment with " + barber.getName(),
                "Haircut appointment at the barbershop",
                "123 Barber St, YourCity", // Replace with your actual location or leave dynamic
                date,
                time,
                endTime
        );

        String body = String.format(
                """
                        Hello %s,
                        
                        Your appointment with %s is confirmed for %s at %s.
                        
                        You can add this to your calendar by clicking the link below:
                        %s
                        
                        Thank you!""",
                customer.getName(),
                barber.getName(),
                date.format(DATE_FORMATTER),
                time.format(TIME_FORMATTER),
                googleCalendarLink
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
