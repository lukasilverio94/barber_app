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

    public void generateMonthlyTimeslots(UUID barberId) {
        Barber barber = barberRepository.findById(barberId)
                .orElseThrow(() -> new IllegalArgumentException("Barber not found"));

        LocalDate today = LocalDate.now();
        LocalDate endDate = today.plusWeeks(1);

        for (LocalDate date = today; !date.isAfter(endDate); date = date.plusDays(1)) {
            if (date.getDayOfWeek() == DayOfWeek.SUNDAY) continue;

            LocalTime startTime = LocalTime.of(8, 0);
            LocalTime endOfDay = LocalTime.of(20, 0);

            while (startTime.plusMinutes(30).isBefore(endOfDay.plusSeconds(1))) {
                LocalTime endTime = startTime.plusMinutes(30);

                if (!timeSlotRepository.existsByDayAndStartTimeAndEndTimeAndBarber(date, startTime, endTime, barber)) {
                    Timeslot slot = new Timeslot();
                    slot.setDay(date);
                    slot.setStartTime(startTime);
                    slot.setEndTime(endTime);
                    slot.setBarber(barber);
                    slot.setTimeslotAvailability(TimeslotAvailability.AVAILABLE);
                    timeSlotRepository.save(slot);
                }

                startTime = startTime.plusMinutes(30);
            }
        }
    }

    public List<Timeslot> getAvailableSlots(UUID barberId) {
        return timeSlotRepository.findByBarberIdAndTimeslotAvailability(barberId, TimeslotAvailability.AVAILABLE);
    }
}
