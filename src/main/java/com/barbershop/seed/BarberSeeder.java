package com.barbershop.seed;

import com.barbershop.model.Barber;
import com.barbershop.repository.BarberRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Profile;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

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
            barber.setName("Admin Barber");
            barber.setPhone("+55313131313");
            barber.setEmail("admin@admin.com");
            // todo: on production remove this hardcode password
            barber.setPassword(passwordEncoder.encode("admin123"));
            repository.save(barber);
            System.out.println("Barber Admin foi criado com sucesso!");
            System.out.println("Barber ID = " + barber.getId());
        }
        else {
            System.out.println("Admin Barber já existe. Seeding ignorado");
        }
    }
}
