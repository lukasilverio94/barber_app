package com.barbershop.seed;

import com.barbershop.enums.ServiceType;
import com.barbershop.model.Barber;
import com.barbershop.repository.BarberRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Profile;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.Set;
import java.util.UUID;

@Component
@Profile({"test", "dev"})
public class BarberSeeder implements CommandLineRunner {

    private final BarberRepository repository;
    private final PasswordEncoder passwordEncoder;

    public BarberSeeder(BarberRepository repository, PasswordEncoder passwordEncoder) {
        this.repository = repository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public void run(String... args) throws Exception {

        if (!repository.existsByName("Admin Barber")) {
            Barber barber = new Barber();
            barber.setId(UUID.fromString("d8493d62-30e2-400d-827a-7e271011074e"));
            barber.setName("Admin Barber");
            barber.setPhone("+5515996452365");
            barber.setEmail("admin@admin.com");
            barber.setPassword(passwordEncoder.encode("admin123"));
            barber.setServiceType(Set.of(ServiceType.HAIRCUT, ServiceType.BEARD));
            repository.save(barber);
            System.out.println("Barber Admin foi criado com sucesso!");
            System.out.println("Barber ID = " + barber.getId());
        }
        else {
            System.out.println("Admin Barber já existe. Seeding ignorado");
        }
    }
}
