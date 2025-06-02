package com.barbershop.service;

import com.barbershop.dto.BarberDTO;
import com.barbershop.exception.BarberNotFoundException;
import com.barbershop.model.Barber;
import com.barbershop.repository.BarberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class BarberService {

    private final BarberRepository barberRepository;

    public Barber createBarber(BarberDTO dto) {
        Barber barber = new Barber();
        barber.setName(dto.name());
        barber.setPhone(dto.phone());
            return barberRepository.save(barber);
    }

    public Barber findBarberById(UUID id) {
        return barberRepository.findById(id)
                .orElseThrow(() -> new BarberNotFoundException(id));
    }
}
