package com.barbershop.service;

import com.barbershop.dto.BarberDTO;
import com.barbershop.dto.mappers.BarberMapper;
import com.barbershop.model.Barber;
import com.barbershop.repository.BarberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.DayOfWeek;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class BarberService {

    private final BarberRepository barberRepository;


    public Barber createBarber(BarberDTO dto) {
        Barber barber = new Barber();
        barber.setName(dto.name());
        barber.setPhone(dto.phone());
        //barber.setServicesOffered(List.of(ServiceType.CORTE, ServiceType.BARBA));

        // Convert DTO map to TimeRange
        /*Map<DayOfWeek, Timeslot> availability = dto.availableDays().entrySet().stream()
                .collect(Collectors.toMap(
                        Map.Entry::getKey,
                        e -> new Timeslot()
                ));
        */
        //barber.setAvailableDays(availability);
        return barberRepository.save(barber);
    }



    public List<BarberDTO> getAllBarbers() {
        return barberRepository.findAll()
                .stream()
                .map(BarberMapper::toDTO)
                .collect(Collectors.toList());
    }


    public boolean isBarberAvailable(UUID barberId, LocalDateTime dateTime) {
        return barberRepository.findById(barberId)
                .map(barber -> {
                    DayOfWeek day = dateTime.getDayOfWeek();
                    LocalTime time = dateTime.toLocalTime();
                    return true;/*timeslot != null &&
                            !time.isBefore(timeslot.getStartTime()) &&
                            !time.isAfter(timeslot.getEndTime());*/
                })
                .orElse(false);
    }
}
