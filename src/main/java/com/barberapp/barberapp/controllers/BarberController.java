package com.barberapp.barberapp.controllers;

import com.barberapp.barberapp.dtos.BarberDTO;
import com.barberapp.barberapp.models.Barber;
import com.barberapp.barberapp.services.BarberService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/barbers")
@RequiredArgsConstructor
public class BarberController {

    private final BarberService barberService;

    @PostMapping
    public ResponseEntity<BarberDTO> createBarber(@RequestBody Barber barber) {
        BarberDTO saved = barberService.createBarber(barber);
        return new ResponseEntity<>(saved, HttpStatus.CREATED);
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
}
