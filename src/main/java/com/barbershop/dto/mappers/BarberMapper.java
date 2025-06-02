package com.barbershop.dto.mappers;

import com.barbershop.dto.BarberDTO;
import com.barbershop.enums.ServiceType;
import com.barbershop.model.Barber;

import java.util.Set;
import java.util.stream.Collectors;

public class BarberMapper {

    public static BarberDTO toDTO(Barber barber) {
        Set<String> serviceTypeDescription = barber.getServiceType()
                .stream()
                .map(ServiceType::getPortugueseDescription)
                .collect(Collectors.toSet());

        return new BarberDTO(
                barber.getId(),
                barber.getName(),
                barber.getPhone(),
                serviceTypeDescription
        );
    }

}
