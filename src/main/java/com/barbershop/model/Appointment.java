package com.barbershop.model;

import com.barbershop.enums.AppointmentStatus;
import com.barbershop.enums.ServiceType;
import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDateTime;

@Entity
@Data
public class Appointment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.EAGER)
    private Barber barber;

    @ManyToOne(fetch = FetchType.EAGER)
    private Customer customer;

    private LocalDateTime dateTime;

    @Enumerated(value = EnumType.STRING)
    private ServiceType serviceType;

    @Enumerated(EnumType.STRING)
    private AppointmentStatus status; // Enum for REQUESTED, CONFIRMED, CANCELLED


}

