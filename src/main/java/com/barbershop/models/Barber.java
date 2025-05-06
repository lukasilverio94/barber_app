package com.barbershop.models;

import jakarta.persistence.*;
import lombok.Data;

import java.time.DayOfWeek;
import java.util.List;
import java.util.Map;

@Entity
@Data
public class Barber {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;
    private String phone;

    @ElementCollection
    @MapKeyEnumerated
    @Column(name = "availability")
    private Map<DayOfWeek, TimeRange> availableDays;

    @OneToMany(mappedBy = "barber")
    private List<Appointment> appointments;

    @ElementCollection
    private List<String> servicesOffered;
}
