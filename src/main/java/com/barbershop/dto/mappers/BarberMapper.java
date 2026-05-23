package com.barbershop.dto.mappers;

import com.barbershop.dto.BarberRequestDTO;
import com.barbershop.dto.BarberResponseDTO;
import com.barbershop.enums.ServiceType;
import com.barbershop.model.Barber;

import java.util.Set;
import java.util.stream.Collectors;

public class BarberMapper {

    public static BarberRequestDTO toDTO(Barber barber) {
        Set<String> serviceTypeDescription = barber.getServiceType()
                .stream()
                .map(ServiceType::getPortugueseDescription)
                .collect(Collectors.toSet());

        return new BarberRequestDTO(
                barber.getName(),
                barber.getPhone(),
                serviceTypeDescription
        );
    }

    public static BarberResponseDTO toResponseDTO(Barber barber) {
        return new BarberResponseDTO(
                barber.getId(),
                barber.getName(),
                barber.getPhone(),
                barber.getEmail(),
                barber.getServiceType()
        );
    }
}
