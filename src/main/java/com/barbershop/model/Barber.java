package com.barbershop.model;

import com.barbershop.enums.ServiceType;
import jakarta.persistence.*;
import lombok.Data;

import java.time.DayOfWeek;
import java.util.List;
import java.util.Map;

@Data
@Entity
@DiscriminatorValue(value = "BARBER")
public class Barber extends AppUser {

}
