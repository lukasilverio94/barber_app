package com.barbershop.repository;

import com.barbershop.enums.ServiceType;
import com.barbershop.factory.BarberFactory;
import com.barbershop.integration.AbstractPostgresContainerTest;
import com.barbershop.model.Barber;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;
import java.util.Optional;
import java.util.Set;


public class BarberRepositoryTest extends AbstractPostgresContainerTest {

    @Autowired
    private BarberRepository barberRepository;

    @Test
    public void BarberRepository_SaveBarber_ReturnSavedBarber() {
        Barber barber = BarberFactory.createDefaultBarber();

        Barber saved = barberRepository.save(barber);

        Assertions.assertThat(saved).isNotNull();
        Assertions.assertThat(saved.getId()).isNotNull();
        Assertions.assertThat(saved.getServiceType()).contains(ServiceType.HAIRCUT);
    }

    @Test
    public void BarberRepository_GetAll_ReturnsBarberList() {
        Barber b1 = BarberFactory.createBarber("A", "a@barber.com", "+1", Set.of(ServiceType.HAIRCUT));
        Barber b2 = BarberFactory.createBarber("B", "b@barber.com", "+2", Set.of(ServiceType.BEARD));

        barberRepository.save(b1);
        barberRepository.save(b2);

        List<Barber> barbers = barberRepository.findAll();

        Assertions.assertThat(barbers).hasSizeGreaterThanOrEqualTo(2);
    }

    @Test
    public void BarberRepository_FindById_ReturnsBarber() {
        Barber barber = BarberFactory.createDefaultBarber();
        barberRepository.save(barber);

        Optional<Barber> result = barberRepository.findById(barber.getId());

        Assertions.assertThat(result).isPresent();
        Assertions.assertThat(result.get().getName()).isEqualTo(barber.getName());
    }

    @Test
    public void BarberRepository_ExistsByName_ReturnsTrue() {
        Barber barber = BarberFactory.createBarber("Johnny Fade", "jf@barber.com", "+333", Set.of(ServiceType.HAIRCUT));
        barberRepository.save(barber);

        boolean exists = barberRepository.existsByName("Johnny Fade");

        Assertions.assertThat(exists).isTrue();
    }

    @Test
    public void BarberRepository_UpdateBarber_ReturnsUpdatedBarber() {
        Barber barber = BarberFactory.createDefaultBarber();
        barberRepository.save(barber);

        Barber toUpdate = barberRepository.findById(barber.getId()).get();
        toUpdate.setName("Updated Barber");
        toUpdate.setServiceType(Set.of(ServiceType.HAIRCUT));

        Barber updated = barberRepository.save(toUpdate);

        Assertions.assertThat(updated.getName()).isEqualTo("Updated Barber");
        Assertions.assertThat(updated.getServiceType()).contains(ServiceType.HAIRCUT);
    }

    @Test
    public void BarberRepository_DeleteBarber_ReturnsEmpty() {
        Barber barber = BarberFactory.createDefaultBarber();
        barberRepository.save(barber);

        barberRepository.deleteById(barber.getId());

        Optional<Barber> result = barberRepository.findById(barber.getId());

        Assertions.assertThat(result).isEmpty();
    }
}
