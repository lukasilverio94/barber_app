package com.barbershop.service;

import com.barbershop.dto.AppointmentCreateDTO;
import com.barbershop.dto.AppointmentResponseDTO;
import com.barbershop.dto.mappers.AppointmentMapper;
import com.barbershop.enums.AppointmentStatus;
import com.barbershop.exception.AppointmentNotFoundException;
import com.barbershop.exception.OutsideBusinessHoursException;
import com.barbershop.model.Appointment;
import com.barbershop.model.Barber;
import com.barbershop.model.Customer;
import com.barbershop.repository.AppointmentRepository;
import com.barbershop.validation.AppointmentValidator;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.UUID;

import static com.barbershop.dto.mappers.AppointmentMapper.toDto;

@Service
@RequiredArgsConstructor
public class AppointmentService {

    private final AppointmentRepository appointmentRepository;
    private final CustomerService customerService;
    private final BarberService barberService;
    private final AppointmentValidator appointmentValidator;
    private final NotificationService notificationService;

    @Transactional
    public AppointmentResponseDTO createAppointment(AppointmentCreateDTO dto) {
        UUID customerId = UUID.fromString(dto.customerId().toString());
        UUID barberId = UUID.fromString(dto.barberId().toString());

        Customer customer = customerService.findCustomerByIdOrThrow(customerId);
        Barber barber = barberService.findBarberByIdOrThrow(barberId);

        LocalDate appointmentDate = dto.date();
        LocalTime appointmentTime = dto.startTime();

        var isDateTimeValid = appointmentValidator.isWhithinBusinessHours(appointmentTime, appointmentDate);

        if (!isDateTimeValid) {
            throw new OutsideBusinessHoursException(appointmentTime, appointmentDate);
        }

        appointmentValidator.validateBarberAvailability(barberId, appointmentDate, appointmentTime);

        Appointment appointment = AppointmentMapper.fromCreateDto(dto, barber, customer);

        return AppointmentMapper.toDto(appointmentRepository.save(appointment));
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

        notificationService.sendAppointmentConfirmation(
                appointment.getCustomer(),
                appointment.getBarber(),
                appointment.getApptDay(),
                appointment.getStartTime()
        );
        return appointment;
    }

    public List<AppointmentResponseDTO> getAppointmentsByCustomer(UUID customerId) {
        return appointmentRepository.findByCustomerIdFetchCustomer(customerId).stream()
                .map(AppointmentMapper::toDto)
                .toList();
    }

    @Transactional
    public AppointmentResponseDTO cancelAppointment(UUID id) {
        Appointment appointment = appointmentRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Appointment not found"));

        appointment.setStatus(AppointmentStatus.CANCELED);
        appointmentRepository.save(appointment);

        notificationService.sendAppointmentCancellation(
                appointment.getCustomer(),
                appointment.getBarber(),
                appointment.getApptDay(),
                appointment.getStartTime()
        );

        return toDto(appointment);
    }

    @Transactional
    public void deleteAppointment(UUID id) {
        Appointment foundAppointment = appointmentRepository.findById(id)
                .orElseThrow(() -> new AppointmentNotFoundException(id));

        appointmentRepository.delete(foundAppointment);
    }

    public List<AppointmentResponseDTO> listAll() {
        return appointmentRepository.findAll()
                .stream().map(AppointmentMapper::toDto).toList();
    }
}
