package com.barbershop.repository;

import com.barbershop.model.Appointment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface AppointmentRepository extends JpaRepository<Appointment, UUID> {
    @Query("""
               SELECT a FROM appointment a 
               JOIN FETCH a.customer
               WHERE a.customer.id = :customerId
            """)
    List<Appointment> findByCustomerIdFetchCustomer(UUID customerId);

    Optional<Appointment> findById(UUID id);

    @Query("""
                SELECT CASE WHEN COUNT(a) > 0 THEN true ELSE false END
                FROM appointment a
                WHERE a.barber.id = :barberId
                  AND a.apptDay = :date
                  AND (
                      (a.startTime < :endTime AND a.endTime > :startTime)
                  )
            """)
    boolean existsByBarberIdAndApptDayAndTimeRange(
            @Param("barberId") UUID barberId,
            @Param("date") LocalDate date,
            @Param("startTime") LocalTime startTime,
            @Param("endTime") LocalTime endTime
    );
}
