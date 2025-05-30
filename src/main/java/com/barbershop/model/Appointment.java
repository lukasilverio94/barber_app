package com.barbershop.model;

import com.barbershop.enums.AppointmentStatus;
import com.barbershop.enums.ServiceType;
import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.UUID;

@Entity(name = "appointment")
@Data
public class Appointment {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID id;

    @Column(name = "appt_day")
    private LocalDate apptDay;

    @Column(name = "start_time")
    private LocalTime startTime;

    @Column(name= "end_time")
    private LocalTime endTime;

    @Column(name = "service")
    @Enumerated(value = EnumType.STRING)
    private ServiceType serviceType;

    @Column(name = "status")
    @Enumerated(EnumType.STRING)
    private AppointmentStatus status;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "barber_id")
    private Barber barber;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "customer_id")
    private Customer customer;

}

