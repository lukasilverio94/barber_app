package com.barbershop.service;

import com.barbershop.dto.AppointmentCreateDTO;
import com.barbershop.dto.AppointmentResponseDTO;
import com.barbershop.dto.mappers.AppointmentMapper;
import com.barbershop.enums.AppointmentEvent;
import com.barbershop.enums.AppointmentStatus;
import com.barbershop.exception.AppointmentNotFoundException;
import com.barbershop.exception.InvalidAppointmentStateException;
import com.barbershop.exception.OutsideBusinessHoursException;
import com.barbershop.model.Appointment;
import com.barbershop.model.Barber;
import com.barbershop.model.Customer;
import com.barbershop.repository.AppointmentRepository;
import com.barbershop.validation.AppointmentValidator;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.UUID;

import static com.barbershop.dto.mappers.AppointmentMapper.toDto;

@Slf4j
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
        Customer customer = customerService.findCustomerByIdOrThrow(dto.customerId());
        Barber barber = barberService.findBarberByIdOrThrow(dto.barberId());
        LocalDate appointmentDate = dto.date();
        LocalTime appointmentTime = dto.startTime();

        appointmentValidator.isWhithinBusinessHoursOrThrow(appointmentTime, appointmentDate);
        appointmentValidator.validateBarberAvailability(barber.getId(), appointmentDate, appointmentTime);

        Appointment appointment = AppointmentMapper.fromCreateDto(dto, barber, customer);
        var saved = appointmentRepository.save(appointment);
        log.info("APPOINTMENT_EVENT={} | id={} | barberId={} | customerId={} | date={} | time={}",
                AppointmentEvent.CREATED,
                saved.getId(),
                saved.getBarber().getId(),
                saved.getCustomer().getId(),
                saved.getApptDay(),
                saved.getStartTime());
        return AppointmentMapper.toDto(appointmentRepository.save(saved));
    }

    @Transactional
    public Appointment acceptAppointment(UUID id) {
        Appointment appointment = appointmentRepository.findById(id)
                .orElseThrow(() -> new AppointmentNotFoundException(id));

        if (appointment.getStatus() == AppointmentStatus.ACCEPTED) {
            throw new InvalidAppointmentStateException("Appointment is already accepted");
        }
        appointment.setStatus(AppointmentStatus.ACCEPTED);
        appointmentRepository.save(appointment);
        log.info("APPOINTMENT_EVENT={} | id={} | barberId={} | customerId={}",
                AppointmentEvent.ACCEPTED,
                appointment.getId(),
                appointment.getBarber().getId(),
                appointment.getCustomer().getId());

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
                .orElseThrow(() -> new AppointmentNotFoundException(id));

        appointment.setStatus(AppointmentStatus.CANCELED);

        log.info("APPOINTMENT_EVENT={} | id={} | barberId={} | customerId={} | date={} | time={}",
                AppointmentEvent.CANCELED,
                appointment.getId(),
                appointment.getBarber().getId(),
                appointment.getCustomer().getId(),
                appointment.getApptDay(),
                appointment.getStartTime());

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
        log.info("APPOINTMENT_EVENT={} appointmentId={} barberId={} customerId={}",
                "DELETED",
                foundAppointment.getId(),
                foundAppointment.getBarber().getId(),
                foundAppointment.getCustomer().getId());
    }

    public List<AppointmentResponseDTO> listAll() {
        return appointmentRepository.findAll()
                .stream().map(AppointmentMapper::toDto).toList();
    }
}
