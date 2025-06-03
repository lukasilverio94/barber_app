package com.barbershop.factory;

import com.barbershop.enums.ServiceType;
import com.barbershop.model.Barber;

import java.util.Set;
import java.util.UUID;
import java.util.concurrent.ThreadLocalRandom;

public class BarberFactory {

    public static Barber createDefaultBarber() {
        Barber barber = new Barber();
        barber.setId(UUID.randomUUID());
        barber.setName("Barber Bob");
        barber.setEmail("bob" + UUID.randomUUID() + "@barbershop.com"); // ensure unique email
        barber.setPhone(generateRandomPhone());
        barber.setPassword("securepass");
        barber.setServiceType(Set.of(ServiceType.HAIRCUT));
        return barber;
    }

    private static String generateRandomPhone() {
        return "+1" + ThreadLocalRandom.current().nextInt(100000000, 999999999);
    }

    public static Barber createBarber(String name, String phone, String email, Set<ServiceType> services) {
        Barber barber = new Barber();
        barber.setId(UUID.randomUUID());
        barber.setName(name);
        barber.setPhone(phone);
        barber.setEmail(email);
        barber.setPassword("securepass");
        barber.setServiceType(services);

        return barber;
    }
}
