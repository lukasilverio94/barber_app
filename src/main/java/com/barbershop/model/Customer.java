package com.barbershop.model;

import jakarta.persistence.*;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.List;

@EqualsAndHashCode(callSuper = true)
@Data
@Entity
@DiscriminatorValue(value = "CUSTOMER")
public class Customer extends AppUser {

    @OneToMany
    private List<Appointment> appointments;
}
