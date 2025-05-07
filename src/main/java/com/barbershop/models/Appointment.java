package com.barbershop.models;

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
    private String serviceType;

    @Enumerated(EnumType.STRING)
    private AppointmentStatus status; // Enum for REQUESTED, CONFIRMED, CANCELLED


}

