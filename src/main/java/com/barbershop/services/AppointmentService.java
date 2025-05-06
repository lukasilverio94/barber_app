package com.barbershop.services;

import com.barbershop.dtos.AppointmentCreateDTO;
import com.barbershop.dtos.AppointmentDTO;
import com.barbershop.dtos.mappers.AppointmentMapper;
import com.barbershop.exceptions.AppointmentNotFoundException;
import com.barbershop.exceptions.BarberNotFoundException;
import com.barbershop.exceptions.ClientNotFoundException;
import com.barbershop.models.Appointment;
import com.barbershop.models.AppointmentStatus;
import com.barbershop.models.Barber;
import com.barbershop.models.Client;
import com.barbershop.repositories.AppointmentRepository;
import com.barbershop.repositories.BarberRepository;
import com.barbershop.repositories.ClientRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.List;

import static com.barbershop.dtos.mappers.AppointmentMapper.toDto;

@Service
@RequiredArgsConstructor
public class AppointmentService {
    private static final Logger logger = LoggerFactory.getLogger(AppointmentService.class);

    private final AppointmentRepository appointmentRepository;
    private final BarberRepository barberRepository;
    private final ClientRepository clientRepository;
    private final NotificationService notificationService;

    public void createAppointment(AppointmentCreateDTO dto) {
        Barber barber = barberRepository.findById(dto.barberId())
                .orElseThrow(() -> new BarberNotFoundException(dto.barberId()));

        Client client = clientRepository.findById(dto.clientId())
                .orElseThrow(() -> new ClientNotFoundException(dto.clientId()));

        Appointment appointment = AppointmentMapper.fromCreateDTO(dto, barber, client);

        String message = String.format(
                "✂️ Pedido de agendamento!\n📅 Data: %s\n🕒 Hora: %s\n✂️ Serviço: %s\n👤 Cliente: %s",
                appointment.getDateTime().toLocalDate(),
                appointment.getDateTime().toLocalTime(),
                appointment.getServiceType(),
                appointment.getClient().getName()
        );

        notificationService.sendWhatsAppMessage(barber.getPhone(), message);

        appointmentRepository.save(appointment);
    }

    public Appointment acceptAppointment(Long id) {
        Appointment appointment = getAppointmentByIdEntity(id);

        if (appointment.getStatus() == AppointmentStatus.ACCEPTED) {
            throw new IllegalStateException("Appointment is already accepted");
        }

        appointment.setStatus(AppointmentStatus.ACCEPTED);
        appointmentRepository.save(appointment);

        String message = String.format(
                "✅ Agendamento Confirmado!\n📅 Date: %s\n🕒 Time: %s\n✂️ Service: %s\n👤 Barber: %s",
                appointment.getDateTime().toLocalDate(),
                appointment.getDateTime().toLocalTime(),
                appointment.getServiceType(),
                appointment.getBarber().getName()
        );

        // Send WhatsApp messages
        notificationService.sendWhatsAppMessage(appointment.getClient().getPhone(), message);
        notificationService.sendWhatsAppMessage(appointment.getBarber().getPhone(), message);

        return appointment;
    }

    public Appointment cancelAppointment(Long id) {
        Appointment appointment = getAppointmentByIdEntity(id);
        appointment.setStatus(AppointmentStatus.CANCELLED);
        appointmentRepository.save(appointment);

        String message = String.format(
                "❌ Your appointment on %s at %s has been cancelled.",
                appointment.getDateTime().toLocalDate(),
                appointment.getDateTime().toLocalTime()
        );

        // Send WhatsApp cancellation to client (optional to notify barber too)
        notificationService.sendWhatsAppMessage(appointment.getClient().getPhone(), message);

        return appointment;
    }

    private Appointment getAppointmentByIdEntity(Long id) {
        return appointmentRepository.findById(id)
                .orElseThrow(() -> {
                    logger.error("Appointment with id {} not found", id);
                    return new AppointmentNotFoundException(id);
                });
    }

    public AppointmentDTO getAppointmentByIdOrThrow(Long id) {
        Appointment appointment = appointmentRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Appointment not found"));

        return toDto(appointment);
    }

    public List<AppointmentDTO> getAllAppointments() {
        return appointmentRepository.findAll()
                .stream()
                .map(AppointmentMapper::toDto)
                .toList();
    }

    @Transactional
    public AppointmentDTO updateAppointment(Long id, Appointment updated) {
        Appointment existing = appointmentRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Appointment not found"));

        existing.setBarber(updated.getBarber());
        existing.setClient(updated.getClient());
        existing.setDateTime(updated.getDateTime());
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
