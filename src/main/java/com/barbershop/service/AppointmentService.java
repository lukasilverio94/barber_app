package com.barbershop.service;

import com.barbershop.dto.AppointmentCreateDTO;
import com.barbershop.dto.AppointmentResponseDTO;
import com.barbershop.dto.mappers.AppointmentMapper;
import com.barbershop.enums.AppointmentStatus;
import com.barbershop.exception.AppointmentNotFoundException;
import com.barbershop.exception.BarberNotAvailableException;
import com.barbershop.exception.BarberNotFoundException;
import com.barbershop.exception.CustomerNotFoundException;
import com.barbershop.model.Appointment;
import com.barbershop.model.Barber;
import com.barbershop.model.Customer;
import com.barbershop.model.Email;
import com.barbershop.repository.AppUserRepository;
import com.barbershop.repository.AppointmentRepository;
import com.barbershop.repository.CustomerRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.UUID;

import static com.barbershop.dto.mappers.AppointmentMapper.toDto;

@Service
@RequiredArgsConstructor
public class AppointmentService {

    private static final LocalTime OPENING_TIME = LocalTime.of(8, 0);
    private static final LocalTime CLOSING_TIME = LocalTime.of(20, 0);
    private static final int APPOINTMENT_DURATION_MINUTES = 30;

    private final AppointmentRepository appointmentRepository;
    private final CustomerRepository customerRepository;
    private final AppUserRepository appUserRepository;
    private final EmailService emailService;

    @Transactional
    public AppointmentResponseDTO createAppointment(AppointmentCreateDTO dto) {
        UUID customerId = UUID.fromString(dto.customerId().toString());
        UUID barberId = UUID.fromString(dto.barberId().toString());

        Customer customer = customerRepository.findById(customerId)
                .orElseThrow(() -> new CustomerNotFoundException(customerId));

        Barber barber = appUserRepository.findBarberById(barberId)
                .orElseThrow(() -> new BarberNotFoundException(barberId));

        LocalDate date = dto.date();
        LocalTime time = dto.startTime();

        validateAppointmentTime(time, date);

        boolean isOverlapping = appointmentRepository.existsByBarberIdAndApptDayAndTimeRange(
                barberId, date, time, time.plusMinutes(30)
        );

        if (isOverlapping) {
            throw new BarberNotAvailableException("Barber not available at this time. Try again");
        }

        Appointment appointment = AppointmentMapper.fromCreateDto(dto, barber, customer);

        appointmentRepository.save(appointment);

        // ✅ Format date and time
        String formattedDate = date.format(DateTimeFormatter.ofPattern("MMMM d, yyyy"));
        String formattedTime = time.format(DateTimeFormatter.ofPattern("hh:mm a"));

        // ✅ Compose message
        String subject = "Appointment Confirmation";
        String body = String.format(
                "Hello %s,\n\nYour appointment with %s is confirmed for %s at %s.\n\nThank you!",
                customer.getName(), barber.getName(), formattedDate, formattedTime
        );

        // ✅ Send email
        Email email = new Email(customer.getEmail(), subject, body);
        emailService.sendSimpleEmail(email);
        return AppointmentMapper.toDto(appointment);
    }


    @Transactional
    public Appointment acceptAppointment(UUID id) {
        Appointment appointment = appointmentRepository.findById(id)
                .orElseThrow(() -> new AppointmentNotFoundException(id));

        if (appointment.getStatus() == AppointmentStatus.ACCEPTED) {
            throw new IllegalStateException("Appointment is already accepted");
        }

        appointment.setStatus(AppointmentStatus.ACCEPTED);
        appointmentRepository.save(appointment);

        return appointment;
    }

    @Transactional
    public List<AppointmentResponseDTO> getAppointmentsByCustomer(UUID customerId) {
        return appointmentRepository.findByCustomerId(customerId).stream()
                .map(AppointmentMapper::toDto)
                .toList();
    }

    @Transactional
    public AppointmentResponseDTO cancelAppointment(UUID id) {
        Appointment appointment = appointmentRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Appointment not found"));

        appointment.setStatus(AppointmentStatus.CANCELED);
        appointmentRepository.save(appointment);

        return toDto(appointment);
    }

    @Transactional
    public void deleteAppointment(UUID id) {
        Appointment foundAppointment = appointmentRepository.findById(id)
                .orElseThrow(() -> new AppointmentNotFoundException(id));

        appointmentRepository.delete(foundAppointment);
    }

    // helper validation appointment
    private void validateAppointmentTime(LocalTime time, LocalDate date) {
        if (time.isBefore(OPENING_TIME) || time.plusMinutes(APPOINTMENT_DURATION_MINUTES).isAfter(CLOSING_TIME)) {
            throw new IllegalArgumentException("Appointments must be between 8AM and 19:30PM");
        }

        if (date.getDayOfWeek() == DayOfWeek.SUNDAY) {
            throw new IllegalArgumentException("Barbershop is closed on Sundays");
        }
    }

    public List<AppointmentResponseDTO> listAll() {
        return appointmentRepository.findAll()
                .stream().map(AppointmentMapper::toDto).toList();
    }
}
