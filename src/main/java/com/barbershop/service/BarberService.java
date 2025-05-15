package com.barbershop.service;

import com.barbershop.dto.BarberDTO;
import com.barbershop.dto.mappers.BarberMapper;
import com.barbershop.enums.ServiceType;
import com.barbershop.exception.BarberNotFoundException;
import com.barbershop.model.Barber;
import com.barbershop.model.Timeslot;
import com.barbershop.repository.BarberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.DayOfWeek;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;
import java.util.Map;
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


    public BarberDTO getBarberDTOById(Long id) {
        return BarberMapper.toDTO(getBarberByIdOrThrow(id));
    }

    public Barber getBarberByIdOrThrow(Long id) {
        return barberRepository.findById(id)
                .orElseThrow(() -> new BarberNotFoundException(id));
    }

    public List<BarberDTO> getAllBarbers() {
        return barberRepository.findAll()
                .stream()
                .map(BarberMapper::toDTO)
                .collect(Collectors.toList());
    }

    public BarberDTO updateBarber(Long id, Barber barberDetails) {
        Barber barber = getBarberByIdOrThrow(id);
        barber.setName(barberDetails.getName());
        barber.setPhone(barberDetails.getPhone());
        //barber.setServicesOffered(barberDetails.getServicesOffered());
        return BarberMapper.toDTO(barberRepository.save(barber));
    }

    public void deleteBarber(Long id) {
        Barber barber = getBarberByIdOrThrow(id);
        barberRepository.delete(barber);
    }

    public boolean isBarberAvailable(Long barberId, LocalDateTime dateTime) {
        return barberRepository.findById(barberId)
                .map(barber -> {
                    DayOfWeek day = dateTime.getDayOfWeek();
                    LocalTime time = dateTime.toLocalTime();
                    Timeslot timeslot = null;
                    return true;/*timeslot != null &&
                            !time.isBefore(timeslot.getStartTime()) &&
                            !time.isAfter(timeslot.getEndTime());*/
                })
                .orElse(false);
    }
}
