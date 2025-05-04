package com.barbershop.services;

import com.barbershop.dtos.BarberDTO;
import com.barbershop.exceptions.BarberNotFoundException;
import com.barbershop.dtos.mappers.BarberMapper;
import com.barbershop.models.Barber;
import com.barbershop.repositories.BarberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class BarberService {

    private final BarberRepository barberRepository;

    public List<BarberDTO> getAllBarbers() {
        return barberRepository.findAll()
                .stream()
                .map(BarberMapper::toDTO)
                .collect(Collectors.toList());
    }

    public BarberDTO getBarberDTOById(Long id) {
        return BarberMapper.toDTO(getBarberByIdOrThrow(id));
    }

    public Barber getBarberByIdOrThrow(Long id) {
        return barberRepository.findById(id)
                .orElseThrow(() -> new BarberNotFoundException(id));
    }

    public BarberDTO createBarber(Barber barber) {
        return BarberMapper.toDTO(barberRepository.save(barber));
    }

    public BarberDTO updateBarber(Long id, Barber barberDetails) {
        Barber barber = getBarberByIdOrThrow(id);
        barber.setName(barberDetails.getName());
        barber.setContactInfo(barberDetails.getContactInfo());
        barber.setWorkingHours(barberDetails.getWorkingHours());
        barber.setServicesOffered(barberDetails.getServicesOffered());
        return BarberMapper.toDTO(barberRepository.save(barber));
    }

    public void deleteBarber(Long id) {
        Barber barber = getBarberByIdOrThrow(id);
        barberRepository.delete(barber);
    }
}
