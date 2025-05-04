package com.barberapp.barberapp.services;

import com.barberapp.barberapp.dtos.AppointmentCreateDTO;
import com.barberapp.barberapp.dtos.AppointmentDTO;
import com.barberapp.barberapp.exceptions.AppointmentNotFoundException;
import com.barberapp.barberapp.models.Appointment;
import com.barberapp.barberapp.models.AppointmentStatus;
import com.barberapp.barberapp.models.Barber;
import com.barberapp.barberapp.models.Client;
import com.barberapp.barberapp.repositories.AppointmentRepository;
import com.barberapp.barberapp.repositories.BarberRepository;
import com.barberapp.barberapp.repositories.ClientRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

/*
TODO: crete a mapper dto<-->entity later to reduce boilerplate
 */

@Service
@RequiredArgsConstructor
public class AppointmentService {

    private final AppointmentRepository appointmentRepository;
    private final BarberRepository barberRepository;
    private final ClientRepository clientRepository;

    public AppointmentDTO createAppointment(AppointmentCreateDTO appointmentCreateDTO) {
        Barber barber = barberRepository.findById(appointmentCreateDTO.barberId())
                .orElseThrow(() -> new IllegalArgumentException("Barber not found"));

        Client client = clientRepository.findById(appointmentCreateDTO.clientId())
                .orElseThrow(() -> new IllegalArgumentException("Client not found"));

        // create the Appointment entity from the DTO data

        Appointment appointment = new Appointment();
        appointment.setBarber(barber);
        appointment.setClient(client);
        appointment.setDateTime(appointmentCreateDTO.dateTime());
        appointment.setServiceType(appointmentCreateDTO.serviceType());
        appointment.setStatus(AppointmentStatus.REQUESTED);

        // Save the appointment
        Appointment savedAppointment = appointmentRepository.save(appointment);

        // Return the saved Appointment as a DTO
        return new AppointmentDTO(
                savedAppointment.getId(),
                savedAppointment.getBarber().getName(),
                savedAppointment.getClient().getName(),
                savedAppointment.getDateTime(),
                savedAppointment.getServiceType(),
                savedAppointment.getStatus().toString()
        );
    }

    public AppointmentDTO getAppointmentByIdOrThrow(Long id) {
        Appointment appointment = appointmentRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Appointment not found"));

        // Map Appointment to AppointmentDTO
        return new AppointmentDTO(
                appointment.getId(),
                appointment.getBarber().getName(),
                appointment.getClient().getName(),
                appointment.getDateTime(),
                appointment.getServiceType(),
                appointment.getStatus().toString()
        );
    }

    public List<AppointmentDTO> getAllAppointments() {
        List<Appointment> appointments = appointmentRepository.findAll();

        // Map Appointment entities to AppointmentDTOs
        return appointments.stream()
                .map(appointment -> new AppointmentDTO(
                        appointment.getId(),
                        appointment.getBarber().getName(),
                        appointment.getClient().getName(),
                        appointment.getDateTime(),
                        appointment.getServiceType(),
                        appointment.getStatus().toString()
                ))
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

        // Return updated Appointment as DTO
        return new AppointmentDTO(
                savedAppointment.getId(),
                savedAppointment.getBarber().getName(),
                savedAppointment.getClient().getName(),
                savedAppointment.getDateTime(),
                savedAppointment.getServiceType(),
                savedAppointment.getStatus().toString()
        );
    }

    public void deleteAppointment(Long id) {
        Appointment foundAppointment = appointmentRepository.findById(id)
                .orElseThrow(() -> new AppointmentNotFoundException(id));

        appointmentRepository.delete(foundAppointment);
    }
}
