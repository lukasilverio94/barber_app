package com.barbershop.repository;

import com.barbershop.enums.AppointmentStatus;
import com.barbershop.model.Appointment;
import com.barbershop.model.Barber;
import com.barbershop.model.Customer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AppointmentRepository extends JpaRepository<Appointment, Long> {
    List<Appointment> findByBarber(Barber barber);

    List<Appointment> findByCustomer(Customer customer);

    List<Appointment> findByStatus(AppointmentStatus status);

}
