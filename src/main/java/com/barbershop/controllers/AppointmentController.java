package com.barbershop.controllers;

import com.barbershop.dtos.AppointmentCreateDTO;
import com.barbershop.dtos.AppointmentDTO;
import com.barbershop.dtos.mappers.AppointmentMapper;
import com.barbershop.models.Appointment;
import com.barbershop.services.AppointmentService;
import lombok.RequiredArgsConstructor;
import org.apache.coyote.Response;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/appointments")
@RequiredArgsConstructor
public class AppointmentController {

    private final AppointmentService appointmentService;


    @PostMapping
    public ResponseEntity<AppointmentCreateDTO> createAppointment(@RequestBody AppointmentCreateDTO dto) {
        appointmentService.createAppointment(dto);
        return ResponseEntity.status(201).body(dto);
    }

    @PostMapping("/{id}/accept")
    public ResponseEntity<AppointmentDTO> acceptAppointment(@PathVariable Long id) {
        Appointment appointment = appointmentService.acceptAppointment(id);
        return ResponseEntity.ok(AppointmentMapper.toDto(appointment));
    }


    @PostMapping("/{id}/cancel")
    public ResponseEntity<Appointment> cancelAppointment(@PathVariable Long id) {
        return ResponseEntity.ok(appointmentService.cancelAppointment(id));
    }

    @GetMapping
    public ResponseEntity<List<AppointmentDTO>> getAllAppointments() {
        List<AppointmentDTO> appointmentsDTOs = appointmentService.getAllAppointments();
        return ResponseEntity.ok(appointmentsDTOs);
    }

    @GetMapping("/{id}")
    public ResponseEntity<AppointmentDTO> getAppointmentById(@PathVariable Long id) {
        return ResponseEntity.ok(appointmentService.getAppointmentByIdOrThrow(id));
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
