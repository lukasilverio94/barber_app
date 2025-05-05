package com.barbershop.services;

import com.barbershop.dtos.BarberDTO;
import com.barbershop.dtos.mappers.BarberMapper;
import com.barbershop.exceptions.BarberNotFoundException;
import com.barbershop.models.Barber;
import com.barbershop.models.TimeRange;
import com.barbershop.repositories.BarberRepository;
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
        barber.setContactInfo(dto.contactInfo());
        barber.setServicesOffered(dto.servicesOffered());

        // Convert DTO map to TimeRange
        Map<DayOfWeek, TimeRange> availability = dto.availableDays().entrySet().stream()
                .collect(Collectors.toMap(
                        Map.Entry::getKey,
                        e -> new TimeRange(e.getValue().startTime(), e.getValue().endTime())
                ));

        barber.setAvailableDays(availability);
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
        barber.setContactInfo(barberDetails.getContactInfo());
        barber.setServicesOffered(barberDetails.getServicesOffered());
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
                    TimeRange timeRange = barber.getAvailableDays().get(day);
                    return timeRange != null &&
                            !time.isBefore(timeRange.getStartTime()) &&
                            !time.isAfter(timeRange.getEndTime());
                })
                .orElse(false);
    }
}
