package com.barbershop.service;

import com.barbershop.dto.TimeslotDTO;
import com.barbershop.enums.TimeslotAvailability;
import com.barbershop.model.AppUser;
import com.barbershop.model.Barber;
import com.barbershop.model.Timeslot;
import com.barbershop.repository.TimeSlotRepository;
import com.barbershop.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class TimeSlotService {

    private final TimeSlotRepository timeSlotRepository;
    private final UserRepository userRepository;

    public Timeslot createTimeSlot(TimeslotDTO dto) {
        AppUser user = userRepository.findById(dto.barberId())
                .orElseThrow(() -> new RuntimeException("Barber not found"));

        if (!(user instanceof Barber)) {
            throw new IllegalArgumentException("Only barber users can create timeslots");
        }

        Barber barber = (Barber) user;

        Timeslot timeSlot = new Timeslot();
        timeSlot.setDay(dto.day());
        timeSlot.setStartTime(dto.startTime());
        timeSlot.setEndTime(dto.endTime());
        timeSlot.setBarber(barber);
        timeSlot.setAvailability(TimeslotAvailability.AVAILABLE);

        return timeSlotRepository.save(timeSlot);
    }

    public List<Timeslot> getAvailableTimeslotsByBarber(UUID barberId) {
        return timeSlotRepository.findByBarberIdAndAvailability(barberId, TimeslotAvailability.AVAILABLE);
    }

    public List<Timeslot> getAllTimeslotsByBarber(UUID barberId) {
        return timeSlotRepository.findByBarberId(barberId);
    }

    public Timeslot updateTimeslotAvailability(UUID timeslotId, TimeslotAvailability availability) {
        Timeslot timeslot = timeSlotRepository.findById(timeslotId)
                .orElseThrow(() -> new RuntimeException("Timeslot not found"));

        timeslot.setAvailability(availability);
        return timeSlotRepository.save(timeslot);
    }

    public void deleteTimeslot(UUID timeslotId) {
        Timeslot timeslot = timeSlotRepository.findById(timeslotId)
                .orElseThrow(() -> new RuntimeException("Timeslot not found"));
        timeSlotRepository.delete(timeslot);
    }
}
