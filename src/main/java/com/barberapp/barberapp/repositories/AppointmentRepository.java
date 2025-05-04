package com.barberapp.barberapp.repositories;

import com.barberapp.barberapp.models.Appointment;
import com.barberapp.barberapp.models.AppointmentStatus;
import com.barberapp.barberapp.models.Barber;
import com.barberapp.barberapp.models.Client;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface AppointmentRepository extends JpaRepository<Appointment, Long> {
    List<Appointment> findByBarber(Barber barber);

    List<Appointment> findByClient(Client client);

    List<Appointment> findByStatus(AppointmentStatus status);

    Optional<Appointment> findByIdAndStatus(Long id, AppointmentStatus status);
}
