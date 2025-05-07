package com.barbershop.controller;

import com.barbershop.dto.BarberDTO;
import com.barbershop.model.Barber;
import com.barbershop.service.BarberService;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/api/barbers")
@RequiredArgsConstructor
public class BarberController {

    private final BarberService barberService;

    @PostMapping
    public ResponseEntity<Barber> createBarber(@RequestBody BarberDTO dto) {
        Barber barber = barberService.createBarber(dto);
        return ResponseEntity.ok(barber);
    }

    @GetMapping("/{id}")
    public ResponseEntity<BarberDTO> getBarberById(@PathVariable Long id) {
        return ResponseEntity.ok(barberService.getBarberDTOById(id));
    }

    @GetMapping
    public ResponseEntity<List<BarberDTO>> getAllBarbers() {
        return ResponseEntity.ok(barberService.getAllBarbers());
    }

    @PutMapping("/{id}")
    public ResponseEntity<BarberDTO> updateBarber(@PathVariable Long id, @RequestBody Barber barberDetails) {
        return ResponseEntity.ok(barberService.updateBarber(id, barberDetails));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteBarber(@PathVariable Long id) {
        barberService.deleteBarber(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/{barberId}/available")
    public boolean isAvailable(
            @PathVariable Long barberId,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)LocalDateTime dateTime
            ) {
        return barberService.isBarberAvailable(barberId, dateTime);
    }
}
