package com.barbershop.models;

import jakarta.persistence.*;
import lombok.Data;

import java.util.List;

@Entity
@Data
public class Barber {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;
    private String contactInfo;
    private String workingHours; // format: "HH:mm-HH:mm"

    @OneToMany(mappedBy = "barber")
    private List<Appointment> appointments;

    @ElementCollection
    private List<String> servicesOffered;
}
