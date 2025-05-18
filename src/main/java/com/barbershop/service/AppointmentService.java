package com.barbershop.service;

import com.barbershop.dto.AppointmentCreateDTO;
import com.barbershop.dto.AppointmentDTO;
import com.barbershop.dto.mappers.AppointmentMapper;
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
    public AppointmentDTO createAppointment(AppointmentCreateDTO dto) {
        UUID customerId = UUID.fromString(String.valueOf(dto.customerId()));
        UUID barberId = UUID.fromString(String.valueOf(dto.barberId()));

        Customer customer = customerRepository.findById(customerId)
                .orElseThrow(() -> new CustomerNotFoundException(customerId));

        LocalDate date = LocalDate.parse(dto.date());
        LocalTime time = LocalTime.parse(dto.time());

        validateAppointmentTime(LocalTime.parse(dto.time()), LocalDate.parse(dto.date()));

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
            sendNotificationToCustomer(appointment);
        }
        catch(Exception ex) {
            System.out.println("Failed to send notification for appointment " + appointment.getId());
        }

        appointmentRepository.save(appointment);
        return AppointmentMapper.toDto(appointment);
    }

    private void validateAppointmentTime(LocalTime time, LocalDate date) {
        if (time.isBefore(LocalTime.of(8, 0)) || time.isAfter(LocalTime.of(19, 0))) {
            throw new IllegalArgumentException("Appointments must be between 8AM and 8PM");
        }

        if (date.getDayOfWeek() == DayOfWeek.SUNDAY) {
            throw new IllegalArgumentException("Barbershop is closed on Sundays");
        }
    }

    private void sendNotificationToCustomer(Appointment appointment) {
        String message = String.format(
                "✂️ Pedido de agendamento!\n📅 Data: %s\n🕒 Hora: %s\n✂️ Serviço: %s\n👤 Cliente: %s",
                appointment.getDay(),
                appointment.getStartTime(),
                appointment.getServiceType(),
                appointment.getCustomer().getName()
        );
        notificationService.sendWhatsAppMessage(appointment.getCustomer().getPhone(), message);
    }

    // TODO: fix and implement to accept appointment later
//    public Appointment acceptAppointment(Long id) {
//        Appointment appointment = getAppointmentByIdEntity(id);
//
//        if (appointment.getStatus() == AppointmentStatus.ACCEPTED) {
//            throw new IllegalStateException("Appointment is already accepted");
//        }
//
//        appointment.setStatus(AppointmentStatus.ACCEPTED);
//        appointmentRepository.save(appointment);
//
//        String message = String.format(
//                "✅ Agendamento Confirmado!\n📅 Date: %s\n🕒 Time: %s\n✂️ Service: %s\n👤 Barber: %s",
//                appointment.getDay(),
//                appointment.getStartTime(),
//                appointment.getServiceType(),
//                appointment.getBarber().getName()
//        );
//
//        // Send WhatsApp messages
//        notificationService.sendWhatsAppMessage(appointment.getCustomer().getPhone(), message);
//        notificationService.sendWhatsAppMessage(appointment.getBarber().getPhone(), message);
//
//        return appointment;
//    }
//    TODO: fix and implement to cancel appointment later
//    public Appointment cancelAppointment(Long id) {
//        Appointment appointment = getAppointmentByIdEntity(id);
//        appointment.setStatus(AppointmentStatus.CANCELLED);
//        appointmentRepository.save(appointment);
//
//        String message = String.format(
//                "❌ Your appointment on %s at %s has been cancelled.",
//                appointment.getDay(),
//                appointment.getStartTime()
//        );
//
//        // Send WhatsApp cancellation to client (optional to notify barber too)
//        notificationService.sendWhatsAppMessage(appointment.getCustomer().getPhone(), message);
//
//        return appointment;
//    }


    public List<AppointmentDTO> getAppointmentsByCustomer(UUID customerId) {
        return appointmentRepository.findByCustomerId(customerId).stream()
                .map(AppointmentMapper::toDto)
                .toList();
    }

    @Transactional
    public AppointmentDTO updateAppointment(Long id, Appointment updated) {
        Appointment existing = appointmentRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Appointment not found"));

        existing.setBarber(updated.getBarber());
        existing.setCustomer(updated.getCustomer());
        //existing.setDateTime(updated.getDateTime());
        existing.setServiceType(updated.getServiceType());
        existing.setStatus(updated.getStatus());

        Appointment savedAppointment = appointmentRepository.save(existing);

        return toDto(savedAppointment);
    }

    public void deleteAppointment(Long id) {
        Appointment foundAppointment = appointmentRepository.findById(id)
                .orElseThrow(() -> new AppointmentNotFoundException(id));

        appointmentRepository.delete(foundAppointment);
    }


}
