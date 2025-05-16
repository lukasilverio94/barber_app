package com.barbershop.service;

import com.barbershop.enums.TimeslotAvailability;
import com.barbershop.model.Barber;
import com.barbershop.model.Timeslot;
import com.barbershop.repository.BarberRepository;
import com.barbershop.repository.TimeslotRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class TimeslotService {

    private final TimeslotRepository timeSlotRepository;
    private final BarberRepository barberRepository;

    public void generateWeeklyTimeslots(UUID barberId) {
        Barber barber = barberRepository.findById(barberId)
                .orElseThrow(() -> new IllegalArgumentException("Barber not found"));

        LocalDate today = LocalDate.now();
        LocalDate endDate = today.plusWeeks(1);

        for (LocalDate date = today; !date.isAfter(endDate); date = date.plusDays(1)) {
            if (date.getDayOfWeek() == DayOfWeek.SUNDAY) continue;

            for (int hour = 8; hour < 20; hour++) {
                Timeslot slot = new Timeslot();
                slot.setDay(date);
                slot.setStartTime(LocalTime.of(hour, 0));
                slot.setEndTime(LocalTime.of(hour + 1, 0));
                slot.setBarber(barber);
                slot.setTimeslotAvailability(TimeslotAvailability.AVAILABLE);

                // prevent duplicates (constraint will help but still...)
                if(!timeSlotRepository.existsByDayAndStartTimeAndEndTimeAndBarber(date, slot.getStartTime(), slot.getEndTime(), barber)) {
                    timeSlotRepository.save(slot);
                }
            }
        }
    }

    public List<Timeslot> getAvailableSlots(UUID barberId) {
        return timeSlotRepository.findByBarberIdAndTimeslotAvailability(barberId, TimeslotAvailability.AVAILABLE);
    }
}
