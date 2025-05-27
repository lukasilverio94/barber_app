package com.barbershop.service;

import com.barbershop.dto.AppointmentCreateDTO;
import com.barbershop.dto.AppointmentResponseDTO;
import com.barbershop.dto.mappers.AppointmentMapper;
import com.barbershop.enums.AppointmentStatus;
import com.barbershop.enums.TimeslotAvailability;
import com.barbershop.exception.AppointmentNotFoundException;
import com.barbershop.exception.CustomerNotFoundException;
import com.barbershop.model.Appointment;
import com.barbershop.model.Customer;
import com.barbershop.model.Timeslot;
import com.barbershop.repository.AppointmentRepository;
import com.barbershop.repository.CustomerRepository;
import com.barbershop.repository.TimeslotRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.UUID;

import static com.barbershop.dto.mappers.AppointmentMapper.toDto;

@Service
@RequiredArgsConstructor
public class AppointmentService {

    private final AppointmentRepository appointmentRepository;
    private final CustomerRepository customerRepository;
    private final TimeslotRepository timeslotRepository;
    private final NotificationService notificationService;

    @Transactional
    public AppointmentResponseDTO createAppointment(AppointmentCreateDTO dto) {
        UUID customerId = UUID.fromString(String.valueOf(dto.customerId()));
        UUID barberId = UUID.fromString(String.valueOf(dto.barberId()));

        Customer customer = customerRepository.findById(customerId)
                .orElseThrow(() -> new CustomerNotFoundException(customerId));

        LocalDate date = dto.date();
        LocalTime time = dto.startTime();

        if (dto.date() == null) {
            throw new IllegalArgumentException("Appointment date is required.");
        }

        validateAppointmentTime(dto.startTime(), dto.date());

        Timeslot timeslot = timeslotRepository
                .findByDayAndStartTimeAndBarberId(date, time, barberId)
                .orElseThrow(() -> new IllegalArgumentException("Timeslot not found"));

        if (timeslot.getTimeslotAvailability() != TimeslotAvailability.AVAILABLE) {
            throw new IllegalStateException("Timeslot is not available");
        }

        timeslot.setTimeslotAvailability(TimeslotAvailability.UNAVAILABLE);

        Appointment appointment = AppointmentMapper.fromCreateDto(
                dto,
                timeslot.getBarber(),
                customer,
                timeslot
        );

        try {
            notificationService.notifyNewAppointmentRequestToCustomer(appointment);
            notificationService.notifyNewAppointmentRequestToBarber(appointment);
        }
        catch(Exception ex) {
            System.out.println("Failed to send notification for appointment " + appointment.getId());
        }

        appointmentRepository.save(appointment);
        return AppointmentMapper.toDto(appointment);
    }

    public List<AppointmentResponseDTO> listAll() {
        var result = appointmentRepository.findAll();
        return result.stream().map(AppointmentMapper::toDto).toList();
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

        notificationService.notifyAppointmentAccepted(appointment);

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

        notificationService.notifyAppointmentCanceled(appointment);

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
        if (time.isBefore(LocalTime.of(8, 0)) || time.isAfter(LocalTime.of(19, 0))) {
            throw new IllegalArgumentException("Appointments must be between 8AM and 8PM");
        }

        if (date.getDayOfWeek() == DayOfWeek.SUNDAY) {
            throw new IllegalArgumentException("Barbershop is closed on Sundays");
        }
    }

}
