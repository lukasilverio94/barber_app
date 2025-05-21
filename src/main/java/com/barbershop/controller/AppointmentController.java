package com.barbershop.controller;

import com.barbershop.dto.AppointmentCreateDTO;
import com.barbershop.dto.AppointmentResponseDTO;
import com.barbershop.model.Appointment;
import com.barbershop.service.AppointmentService;
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
    public ResponseEntity<AppointmentResponseDTO> createAppointment(@RequestBody AppointmentCreateDTO request) {
        AppointmentResponseDTO createdAppointment = appointmentService.createAppointment(request);
        return ResponseEntity.ok(createdAppointment);
    }

    @GetMapping("/{customerId}")
    public ResponseEntity<List<AppointmentResponseDTO>> getAppointmentsByCustomer(@PathVariable String customerId) {
        List<AppointmentResponseDTO> appointments = appointmentService.getAppointmentsByCustomer(UUID.fromString(customerId));
        return ResponseEntity.ok(appointments);
    }

    @GetMapping
    public ResponseEntity<List<AppointmentResponseDTO>> listAllAppointments() {
        List<AppointmentResponseDTO> appointments = appointmentService.listAll();
        return ResponseEntity.ok(appointments);
    }


    @PutMapping("/{id}")
    public ResponseEntity<AppointmentResponseDTO> updateAppointment(@PathVariable Long id, @RequestBody Appointment appointment) {
        AppointmentResponseDTO updatedAppointment = appointmentService.updateAppointment(id, appointment);
        return ResponseEntity.ok(updatedAppointment);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteAppointment(@PathVariable Long id) {
        appointmentService.deleteAppointment(id);
        return ResponseEntity.noContent().build();
    }
}
