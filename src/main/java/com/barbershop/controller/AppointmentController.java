package com.barbershop.controller;

import com.barbershop.dto.AppointmentCreateDTO;
import com.barbershop.dto.AppointmentDTO;
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
    public ResponseEntity<AppointmentDTO> createAppointment(@RequestBody AppointmentCreateDTO request) {
        AppointmentDTO createdAppointment = appointmentService.createAppointment(request);
        return ResponseEntity.ok(createdAppointment);
    }

    @GetMapping("/{customerId}")
    public ResponseEntity<List<AppointmentDTO>> getAppointmentsByCustomer(@PathVariable String customerId) {
        List<AppointmentDTO> appointments = appointmentService.getAppointmentsByCustomer(UUID.fromString(customerId));
        return ResponseEntity.ok(appointments);
    }


    @PutMapping("/{id}")
    public ResponseEntity<AppointmentDTO> updateAppointment(@PathVariable Long id, @RequestBody Appointment appointment) {
        AppointmentDTO updatedAppointment = appointmentService.updateAppointment(id, appointment);
        return ResponseEntity.ok(updatedAppointment);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteAppointment(@PathVariable Long id) {
        appointmentService.deleteAppointment(id);
        return ResponseEntity.noContent().build();
    }
}
