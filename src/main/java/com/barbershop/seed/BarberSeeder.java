package com.barbershop.seed;

import com.barbershop.model.Barber;
import com.barbershop.repository.BarberRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
public class BarberSeeder implements CommandLineRunner {

    private final BarberRepository repository;

    public BarberSeeder(BarberRepository repository) {
        this.repository = repository;
    }

    @Override
    public void run(String... args) throws Exception {


        if (!repository.existsByName("Admin Barber")) {
            Barber barber = new Barber();
            barber.setName("Admin Barber");
            barber.setPhone("+55313131313");
            barber.setEmail("admin@admin.com");

            repository.save(barber);
            System.out.println("Barber Admin foi criado com sucesso!");
            System.out.println("Barber ID = " + barber.getId());


        }
        else {
            System.out.println("Admin Barber já existe. Seeding ignorado");
        }
    }
}
