package com.barberapp.barberapp.services;

import com.barberapp.barberapp.exceptions.BarberNotFoundException;
import com.barberapp.barberapp.models.Barber;
import com.barberapp.barberapp.repositories.BarberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class BarberService {

    private final BarberRepository barberRepository;

    public List<Barber> getAllBarbers() {
        return barberRepository.findAll();
    }

    public Barber getBarberByIdOrThrow(Long id) {
        return barberRepository.findById(id)
                .orElseThrow(() -> new BarberNotFoundException(id));
    }

    public Barber createBarber(Barber barber) {
        return barberRepository.save(barber);
    }

    public Barber updateBarber(Long id, Barber barberDetails) {
        Barber barber = getBarberByIdOrThrow(id);
        barber.setName(barberDetails.getName());
        barber.setContactInfo(barberDetails.getContactInfo());
        barber.setWorkingHours(barberDetails.getWorkingHours());
        barber.setServicesOffered(barberDetails.getServicesOffered());
        return barberRepository.save(barber);
    }

    public void deleteBarber(Long id) {
        Barber barber = getBarberByIdOrThrow(id);
        barberRepository.delete(barber);
    }
}
