package com.barbershop.model;

import com.barbershop.enums.ServiceType;
import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
import lombok.Data;

import java.util.Set;

@Data
@Entity
@DiscriminatorValue(value = "BARBER")
public class Barber extends AppUser {

    private Set<ServiceType> serviceType;

}
