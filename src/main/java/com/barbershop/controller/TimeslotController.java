package com.barbershop.controller;

import com.barbershop.model.Timeslot;
import com.barbershop.service.TimeslotService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/timeslots")
@RequiredArgsConstructor
public class TimeslotController {

    private final TimeslotService timeslotService;

    /**
     * Generate timeslots for 1 week (Monday to Saturday, 8am–8pm)
     */
    @PostMapping("/{barberId}")
    public ResponseEntity<String> generateWeeklySlots(@PathVariable UUID barberId) {
        timeslotService.generateWeeklyTimeslots(barberId);
        return ResponseEntity.ok("Weekly timeslots generated for barber " + barberId);
    }

    /**
     get all available timeslots for a given barber
     */
    @GetMapping("/{barberId}")
    public ResponseEntity<List<Timeslot>> getAvailableSlots(@PathVariable UUID barberId) {
        List<Timeslot> slots = timeslotService.getAvailableSlots(barberId);
        return ResponseEntity.ok(slots);
    }
}
