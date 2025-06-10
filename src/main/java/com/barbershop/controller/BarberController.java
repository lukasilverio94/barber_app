package com.barbershop.controller;

import com.barbershop.dto.BarberDTO;
import com.barbershop.dto.mappers.BarberMapper;
import com.barbershop.model.Barber;
import com.barbershop.service.BarberService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/api/barbers")
@RequiredArgsConstructor
public class BarberController {

    private final BarberService barberService;

    @PostMapping
    public ResponseEntity<Barber> createBarber(@RequestBody @Valid  BarberDTO dto) {
        Barber barber = barberService.createBarber(dto);
        return ResponseEntity.ok(barber);
    }

    @GetMapping("/{id}")
    public ResponseEntity<BarberDTO> getBarberById(@PathVariable UUID id) {
        Barber barber = barberService.findBarberById(id);
        return ResponseEntity.ok(BarberMapper.toDTO(barber));
    }
}
