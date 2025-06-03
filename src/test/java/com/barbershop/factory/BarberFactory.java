package com.barbershop.factory;

import com.barbershop.enums.ServiceType;
import com.barbershop.model.Barber;

import java.util.Set;
import java.util.UUID;

public class BarberFactory {

    public static Barber createDefaultBarber() {
        return createBarber("Barber Test", "test@barbershop.com", "+123456789", Set.of(ServiceType.HAIRCUT));
    }

    public static Barber createBarber(String name, String email, String phone, Set<ServiceType> services) {
        Barber barber = new Barber();
        barber.setId(UUID.randomUUID());
        barber.setName(name);
        barber.setEmail(email);
        barber.setPhone(phone);
        barber.setPassword("securepass");
        barber.setServiceType(services);
        return barber;
    }
}
