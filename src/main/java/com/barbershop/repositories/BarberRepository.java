package com.barbershop.repositories;

import com.barbershop.models.Barber;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BarberRepository extends JpaRepository<Barber, Long> {

}
