package com.barbershop.model;

import com.barbershop.enums.TimeslotAvailability;
import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.UUID;

@Data
@Entity(name = "timeslot")
public class Timeslot {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Enumerated(EnumType.STRING)
    @Column(name = "timeslot_availability", nullable = false)
    private TimeslotAvailability availability = TimeslotAvailability.AVAILABLE;

    @Column(name = "day")
    private LocalDate day;

    @Column(name = "start_time")
    private LocalTime startTime;

    @Column(name = "end_time")
    private LocalTime endTime;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "barber_id", nullable = false)
    private Barber barber;

    @OneToOne(mappedBy = "timeslot", cascade = CascadeType.ALL)
    private Appointment appointment;

}
