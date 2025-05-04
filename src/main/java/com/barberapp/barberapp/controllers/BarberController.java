package com.barberapp.barberapp.controllers;

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
    public ResponseEntity<Barber> createBarber(@RequestBody Barber barber) {
        Barber saved = barberService.createBarber(barber);
        return new ResponseEntity<>(saved, HttpStatus.CREATED);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Barber> getBarberById(@PathVariable Long id) {
        return ResponseEntity.ok(barberService.getBarberByIdOrThrow(id));
    }

    @GetMapping
    public ResponseEntity<List<Barber>> getAllBarbers() {
        List<Barber> barbers = barberService.getAllBarbers();
        return ResponseEntity.ok(barbers);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Barber> updateBarber(@PathVariable Long id, @RequestBody Barber barberDetails) {
        return ResponseEntity.ok(barberService.updateBarber(id, barberDetails));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteBarber(@PathVariable Long id) {
        barberService.deleteBarber(id);
        return ResponseEntity.noContent().build();
    }
}
