package com.barbershop.repository;

import com.barbershop.model.Appointment;
import com.barbershop.model.AppointmentStatus;
import com.barbershop.model.Barber;
import com.barbershop.model.Customer;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface AppointmentRepository extends JpaRepository<Appointment, Long> {
    List<Appointment> findByBarber(Barber barber);

    List<Appointment> findByCustomer(Customer customer);

    List<Appointment> findByStatus(AppointmentStatus status);

    Optional<Appointment> findByIdAndStatus(Long id, AppointmentStatus status);
}
