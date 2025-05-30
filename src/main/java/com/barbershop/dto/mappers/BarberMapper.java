package com.barbershop.dto.mappers;

import com.barbershop.dto.BarberDTO;
import com.barbershop.model.Barber;

public class BarberMapper {

    public static BarberDTO toDTO(Barber barber) {
        return new BarberDTO(
                barber.getId(),
                barber.getName(),
                barber.getPhone(),
                null
        );
    }

}
