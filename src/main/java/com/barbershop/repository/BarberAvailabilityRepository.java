package com.barbershop.repository;

import com.barbershop.model.BarberAvailability;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.DayOfWeek;
import java.util.List;
import java.util.UUID;

public interface BarberAvailabilityRepository extends JpaRepository<BarberAvailability, UUID> {

    List<BarberAvailability> findByBarberIdAndDayOfWeek(UUID barberId, DayOfWeek dayOfWeek);

}
