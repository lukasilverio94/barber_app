package com.barbershop.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.List;

@EqualsAndHashCode(callSuper = true)
@Data
@Entity
@DiscriminatorValue(value = "CUSTOMER")
public class Customer extends AppUser {

    @OneToMany(mappedBy = "customer")
    @JsonIgnore
    private List<Appointment> appointments;

}
