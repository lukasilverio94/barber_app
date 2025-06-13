package com.barbershop.controller;

import com.barbershop.dto.AppointmentCreateDTO;
import com.barbershop.dto.AppointmentResponseDTO;
import com.barbershop.dto.mappers.AppointmentMapper;
import com.barbershop.model.Appointment;
import com.barbershop.service.AppointmentService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/appointments")
@RequiredArgsConstructor
public class AppointmentController {

    private final AppointmentService appointmentService;


    @PostMapping
    public ResponseEntity<AppointmentResponseDTO> createAppointment(@RequestBody @Valid AppointmentCreateDTO request) {
        AppointmentResponseDTO createdAppointment = appointmentService.createAppointment(request);
        return ResponseEntity.ok(createdAppointment);
    }

    @GetMapping("/{customerId}")
    public ResponseEntity<List<AppointmentResponseDTO>> getAppointmentsByCustomer(@PathVariable String customerId) {
        List<AppointmentResponseDTO> appointments = appointmentService.getAppointmentsByCustomer(UUID.fromString(customerId));
        return ResponseEntity.ok(appointments);
    }

    @PatchMapping("/{id}/accept")
    public ResponseEntity<AppointmentResponseDTO> acceptAppointment(@PathVariable UUID id) {
        Appointment accepted = appointmentService.acceptAppointment(id);
        return ResponseEntity.ok(AppointmentMapper.toDto(accepted));
    }

    @GetMapping
    public ResponseEntity<List<AppointmentResponseDTO>> listAllAppointments() {
        List<AppointmentResponseDTO> appointments = appointmentService.listAll();
        return ResponseEntity.ok(appointments);
    }

    @PatchMapping("/{id}/cancel")
    public ResponseEntity<AppointmentResponseDTO> cancelAppointment(@PathVariable UUID id) {
        AppointmentResponseDTO updatedAppointment = appointmentService.cancelAppointment(id);
        return ResponseEntity.ok(updatedAppointment);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteAppointment(@PathVariable UUID id) {
        appointmentService.deleteAppointment(id);
        return ResponseEntity.noContent().build();
    }
}
