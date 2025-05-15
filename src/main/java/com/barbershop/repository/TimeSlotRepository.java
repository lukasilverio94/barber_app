package com.barbershop.repository;

import com.barbershop.enums.TimeslotAvailability;
import com.barbershop.model.Timeslot;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface TimeSlotRepository extends JpaRepository<Timeslot, UUID> {

    List<Timeslot> findByBarberIdAndAvailability(UUID barberId, TimeslotAvailability availability);
    List<Timeslot> findByBarberId(UUID barberId);
}
