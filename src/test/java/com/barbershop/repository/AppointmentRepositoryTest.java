package com.barbershop.repository;

import com.barbershop.factory.AppointmentFactory;
import com.barbershop.factory.BarberFactory;
import com.barbershop.factory.CustomerFactory;
import com.barbershop.model.Appointment;
import com.barbershop.model.Barber;
import com.barbershop.model.Customer;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.jdbc.EmbeddedDatabaseConnection;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.Optional;

@DataJpaTest
@AutoConfigureTestDatabase(connection = EmbeddedDatabaseConnection.H2)
public class AppointmentRepositoryTest {

    @Autowired
    private AppointmentRepository appointmentRepository;

    @Autowired
    private CustomerRepository customerRepository;

    @Autowired
    private BarberRepository barberRepository;

    @Test
    public void AppointmentRepository_Save_ReturnsAppointment() {
        Customer customer = customerRepository.save(CustomerFactory.createDefaultCustomer());
        Barber barber = barberRepository.save(BarberFactory.createDefaultBarber());

        Appointment appointment = AppointmentFactory.createDefaultAppointment(barber, customer);
        Appointment saved = appointmentRepository.save(appointment);

        Assertions.assertThat(saved).isNotNull();
        Assertions.assertThat(saved.getId()).isNotNull();
    }

    @Test
    public void AppointmentRepository_FindByCustomerId_ReturnsAppointments() {
        Customer customer = customerRepository.save(CustomerFactory.createDefaultCustomer());
        Barber barber = barberRepository.save(BarberFactory.createDefaultBarber());

        Appointment appointment = AppointmentFactory.createDefaultAppointment(barber, customer);
        appointmentRepository.save(appointment);

        List<Appointment> result = appointmentRepository.findByCustomerId(customer.getId());

        Assertions.assertThat(result).isNotEmpty();
        Assertions.assertThat(result.get(0).getCustomer().getId()).isEqualTo(customer.getId());
    }

    @Test
    public void AppointmentRepository_FindById_ReturnsAppointment() {
        Customer customer = customerRepository.save(CustomerFactory.createDefaultCustomer());
        Barber barber = barberRepository.save(BarberFactory.createDefaultBarber());

        Appointment appointment = AppointmentFactory.createDefaultAppointment(barber, customer);
        Appointment saved = appointmentRepository.save(appointment);

        Optional<Appointment> result = appointmentRepository.findById(saved.getId());

        Assertions.assertThat(result).isPresent();
    }

    @Test
    public void AppointmentRepository_ExistsByBarberIdAndApptDayAndTimeRange_ReturnsTrue() {
        Customer customer = customerRepository.save(CustomerFactory.createDefaultCustomer());
        Barber barber = barberRepository.save(BarberFactory.createDefaultBarber());

        LocalDate date = LocalDate.of(2025, 6, 2);
        LocalTime startTime = LocalTime.of(10, 0);
        LocalTime endTime = LocalTime.of(11, 0);

        Appointment existing = AppointmentFactory.createAppointmentWithTime(barber, customer, date, startTime, endTime);
        appointmentRepository.save(existing);

        System.out.println("Saved appt startTime: " + existing.getStartTime());
        System.out.println("Saved appt endTime: " + existing.getEndTime());

        boolean conflict = appointmentRepository.existsByBarberIdAndApptDayAndTimeRange(
                barber.getId(), date, LocalTime.of(10, 0), LocalTime.of(11, 0)
        );

        Assertions.assertThat(conflict).isTrue();
    }

    @Test
    public void AppointmentRepository_ExistsByBarberIdAndApptDayAndTimeRange_ReturnsFalse() {
        Customer customer = customerRepository.save(CustomerFactory.createDefaultCustomer());
        Barber barber = barberRepository.save(BarberFactory.createDefaultBarber());

        LocalDate date = LocalDate.of(2025, 6, 2);
        LocalTime startTime = LocalTime.of(10, 0);
        LocalTime endTime = LocalTime.of(11, 0);

        Appointment existing = AppointmentFactory.createAppointmentWithTime(barber, customer, date, startTime, endTime);
        appointmentRepository.save(existing);

        boolean conflict = appointmentRepository.existsByBarberIdAndApptDayAndTimeRange(
                barber.getId(), date, LocalTime.of(11, 0), LocalTime.of(12, 0)
        );

        Assertions.assertThat(conflict).isFalse();
    }

    @Test
    public void AppointmentRepository_Delete_RemovesAppointment() {
        Customer customer = customerRepository.save(CustomerFactory.createDefaultCustomer());
        Barber barber = barberRepository.save(BarberFactory.createDefaultBarber());

        Appointment appointment = AppointmentFactory.createDefaultAppointment(barber, customer);
        Appointment saved = appointmentRepository.save(appointment);

        appointmentRepository.deleteById(saved.getId());

        Optional<Appointment> result = appointmentRepository.findById(saved.getId());

        Assertions.assertThat(result).isEmpty();
    }
}
