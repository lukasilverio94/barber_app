package com.barbershop.model;

import com.barbershop.enums.ServiceType;
import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Set;

@Entity
@Getter
@Setter
@NoArgsConstructor
@DiscriminatorValue(value = "BARBER")
public class Barber extends AppUser {

    private Set<ServiceType> serviceType;

}
