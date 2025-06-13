package com.barbershop.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.List;
import java.util.UUID;

@EqualsAndHashCode(callSuper = true)
@Data
@Entity
@DiscriminatorValue(value = "CUSTOMER")
public class Customer extends AppUser {

    @OneToMany(mappedBy = "customer")
    @JsonIgnore
    private List<Appointment> appointments;

    public Customer(UUID id, String name, String phone, String email, String password, List<Appointment> appointments) {
        super(id, name, phone, email, password);
        this.appointments = appointments;
    }

    public Customer() {

    }
}
