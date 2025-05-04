package com.barberapp.barberapp.repositories;

import com.barberapp.barberapp.models.Barber;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BarberRepository extends JpaRepository<Barber, Long> {

}
