package com.barbershop.dto;

import com.barbershop.enums.ServiceType;

import java.time.DayOfWeek;
import java.util.List;
import java.util.Map;
import java.util.UUID;

public record BarberDTO(
        UUID id,
        String name,
        String phone,
        //Map<DayOfWeek, TimeRangeDTO> availableDays,
        //List<Long> appointmentIds,
        List<ServiceType> servicesOffered
) { }
