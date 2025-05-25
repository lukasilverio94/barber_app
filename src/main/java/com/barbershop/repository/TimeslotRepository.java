package com.barbershop.repository;

import com.barbershop.enums.TimeslotAvailability;
import com.barbershop.model.Barber;
import com.barbershop.model.Timeslot;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface TimeslotRepository extends JpaRepository<Timeslot, UUID> {

    List<Timeslot> findByBarberIdAndTimeslotAvailability(UUID barberId, TimeslotAvailability timeslotAvailability);

    boolean existsByDayAndStartTimeAndEndTimeAndBarber(LocalDate day, LocalTime start, LocalTime end, Barber barber);

    Optional<Timeslot> findByDayAndStartTimeAndBarberId(LocalDate day, LocalTime startTime, UUID barberId);

    List<Timeslot> findByBarberAndDayBetween(Barber barber, LocalDate start, LocalDate endDate);
}
