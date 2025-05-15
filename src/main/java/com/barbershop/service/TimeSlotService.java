package com.barbershop.service;

import com.barbershop.dto.TimeSlotDTO;
import com.barbershop.enums.TimeslotAvailability;
import com.barbershop.enums.UserType;
import com.barbershop.model.AppUser;
import com.barbershop.model.TimeSlot;
import com.barbershop.repository.TimeSlotRepository;
import com.barbershop.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class TimeSlotService {

    private final TimeSlotRepository timeSlotRepository;
    private final UserRepository userRepository;

    public TimeSlot createTimeSlot(TimeSlotDTO dto) {
        AppUser barber = userRepository.findById(dto.barberId())
                .orElseThrow(() -> new RuntimeException("Barber not found"));

//        if (!barber.getType().equals(UserType.BARBER)) {
//            throw new IllegalArgumentException("Only barber users can create timeslots");
//        }

        TimeSlot timeSlot = new TimeSlot();
        timeSlot.setId(UUID.randomUUID());
        timeSlot.setStartTime(dto.startTime());
        timeSlot.setEndTime(dto.endTime());
        timeSlot.setBarber(barber);
        timeSlot.setAvailability(TimeslotAvailability.valueOf(dto.availability()));

        return timeSlotRepository.save(timeSlot);
    }
}
