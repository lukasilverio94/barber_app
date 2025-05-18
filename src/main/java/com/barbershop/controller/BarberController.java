package com.barbershop.controller;

import com.barbershop.dto.BarberDTO;
import com.barbershop.model.Barber;
import com.barbershop.service.BarberService;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.UUID;

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

    @GetMapping("/{barberId}/available")
    public boolean isAvailable(
            @PathVariable UUID barberId,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)LocalDateTime dateTime
            ) {
        return barberService.isBarberAvailable(barberId, dateTime);
    }
}
