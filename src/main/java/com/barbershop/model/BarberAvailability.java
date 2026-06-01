package com.barbershop.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.DayOfWeek;
import java.time.LocalTime;
import java.util.UUID;

@Entity
@Table(name = "barber_availability")
@Getter
@Setter
public class BarberAvailability {

    @Id
    private UUID id;

    @ManyToOne
    @JoinColumn(name = "barber_id")
    private Barber barber;

    @Enumerated(EnumType.STRING)
    @Column(name = "day_of_week")
    private DayOfWeek dayOfWeek;

    private LocalTime startTime;
    private LocalTime endTime;
}
