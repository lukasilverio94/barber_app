package com.barbershop.controller;

import com.barbershop.dto.BarberRequestDTO;
import com.barbershop.dto.BarberResponseDTO;
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
    public ResponseEntity<BarberResponseDTO> createBarber(@RequestBody @Valid BarberRequestDTO dto) {
        Barber barber = barberService.createBarber(dto);
        return ResponseEntity.ok(BarberMapper.toResponseDTO(barber));
    }

    @GetMapping("/{id}")
    public ResponseEntity<BarberRequestDTO> getBarberById(@PathVariable UUID id) {
        Barber barber = barberService.findBarberByIdOrThrow(id);
        return ResponseEntity.ok(BarberMapper.toDTO(barber));
    }
}
