package com.barbershop.repositories;

import com.barbershop.models.Appointment;
import com.barbershop.models.AppointmentStatus;
import com.barbershop.models.Barber;
import com.barbershop.models.Client;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface AppointmentRepository extends JpaRepository<Appointment, Long> {
    List<Appointment> findByBarber(Barber barber);

    List<Appointment> findByClient(Client client);

    List<Appointment> findByStatus(AppointmentStatus status);

    Optional<Appointment> findByIdAndStatus(Long id, AppointmentStatus status);
}
