package com.barbershop.model;

import jakarta.persistence.*;
import lombok.Data;

import java.util.List;

@Data
@Entity
@DiscriminatorValue(value = "CUSTOMER")
public class Customer extends AppUser {

    //private String email;
    //@OneToMany
    //private List<Appointment> appointments;
}
