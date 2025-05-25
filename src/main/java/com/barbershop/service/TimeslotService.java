package com.barbershop.service;

import com.barbershop.enums.TimeslotAvailability;
import com.barbershop.model.Barber;
import com.barbershop.model.Timeslot;
import com.barbershop.repository.BarberRepository;
import com.barbershop.repository.TimeslotRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class TimeslotService {

    private final TimeslotRepository timeSlotRepository;
    private final BarberRepository barberRepository;

    @Async
    public void generateMonthlyTimeslotsAsync(UUID barberId) {
        generateMonthlyTimeslots(barberId);
    }

    public void generateMonthlyTimeslots(UUID barberId) {
        Barber barber = barberRepository.findById(barberId)
                .orElseThrow(() -> new IllegalArgumentException("Barber not found"));

        LocalDate today = LocalDate.now();
        LocalDate endDate = today.plusMonths(1);

        // Fetch all existing slots in the month range
        List<Timeslot> existingSlots = timeSlotRepository.findByBarberAndDayBetween(barber, today, endDate);
        Set<String> existingSlotKeys = existingSlots.stream()
                .map(slot -> slot.getDay() + "|" + slot.getStartTime())
                .collect(Collectors.toSet());

        List<Timeslot> newSlots = new ArrayList<>();

        for (LocalDate date = today; !date.isAfter(endDate); date = date.plusDays(1)) {
            if (date.getDayOfWeek() == DayOfWeek.SUNDAY) continue;

            LocalTime startTime = LocalTime.of(8, 0);
            LocalTime endOfDay = LocalTime.of(20, 0);

            while (startTime.plusMinutes(30).isBefore(endOfDay.plusSeconds(1))) {
                LocalTime endTime = startTime.plusMinutes(30);
                String slotKey = date + "|" + startTime;

                if (!existingSlotKeys.contains(slotKey)) {
                    Timeslot slot = new Timeslot();
                    slot.setDay(date);
                    slot.setStartTime(startTime);
                    slot.setEndTime(endTime);
                    slot.setBarber(barber);
                    slot.setTimeslotAvailability(TimeslotAvailability.AVAILABLE);
                    newSlots.add(slot);
                }

                startTime = startTime.plusMinutes(30);
            }
        }

        if (!newSlots.isEmpty()) {
            timeSlotRepository.saveAll(newSlots);
        }
    }
    public List<Timeslot> getAvailableSlots(UUID barberId) {
        return timeSlotRepository.findByBarberIdAndTimeslotAvailability(barberId, TimeslotAvailability.AVAILABLE);
    }
}
