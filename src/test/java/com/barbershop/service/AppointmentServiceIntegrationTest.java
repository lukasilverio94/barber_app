package com.barbershop.service;

import com.barbershop.BarberappApplication;
import com.barbershop.dto.AppointmentCreateDTO;
import com.barbershop.dto.AppointmentResponseDTO;
import com.barbershop.enums.AppointmentStatus;
import com.barbershop.enums.ServiceType;
import com.barbershop.exception.BarberNotAvailableException;
import com.barbershop.model.Barber;
import com.barbershop.model.Customer;
import com.barbershop.repository.BarberRepository;
import com.barbershop.repository.CustomerRepository;
import com.barbershop.utils.PostgresContainerTest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalTime;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest(classes = BarberappApplication.class)
public class AppointmentServiceIntegrationTest extends PostgresContainerTest {

    @Autowired
    private AppointmentService appointmentService;

    @Autowired
    CustomerRepository customerRepository;

    @Autowired
    BarberRepository barberRepository;

    Customer testCustomer;
    Barber testBarber;
    LocalDate appointmentDate;

    // Appointment Request DTO factory
    private AppointmentCreateDTO createAppointmentDTO(LocalTime startTime, ServiceType serviceType) {
        return new AppointmentCreateDTO(
                testBarber.getId(),
                testCustomer.getId(),
                appointmentDate,
                startTime,
                serviceType
        );
    }

    // helper function
    private LocalDate nextValidBusinessDay(LocalDate from) {
        LocalDate date = from;
        while (date.getDayOfWeek() == DayOfWeek.SUNDAY) {
            date = date.plusDays(1);
        }
        return date;
    }

    @BeforeEach
    void setup() {
        testCustomer = new Customer();
        testCustomer.setName("Test Customer");
        testCustomer.setEmail("customer@example.com");
        testCustomer.setPassword("secret");
        testCustomer.setPhone("1234567890");
        testCustomer = customerRepository.save(testCustomer);

        testBarber = new Barber();
        testBarber.setName("Test Barber");
        testBarber.setEmail("barber@example.com");
        testBarber.setPassword("secret");
        testBarber.setPhone("0987654321");
        testBarber = barberRepository.save(testBarber);

        appointmentDate = nextValidBusinessDay(LocalDate.now().plusDays(1));


        testCustomer = customerRepository.saveAndFlush(testCustomer);

        assertNotNull(testCustomer.getId());
    }

    @Test
    void createAppointment_shouldPersistSuccessfully() {
        var dto = createAppointmentDTO(LocalTime.of(11, 0), ServiceType.HAIRCUT);

        AppointmentResponseDTO response = appointmentService.createAppointment(dto);

        assertNotNull(response);
        assertEquals(testBarber.getId(), response.barberId());
        assertEquals(ServiceType.HAIRCUT, response.serviceType());

        assertEquals(dto.date(), response.day());
        assertEquals(dto.startTime(), response.startTime());

    }

    @Test
    void createAppointment_barberNotAvailable_shouldThrowException() {
        var dto1 = createAppointmentDTO(LocalTime.of(10, 0), ServiceType.HAIRCUT);
        appointmentService.createAppointment(dto1);

        var dto2 = createAppointmentDTO(LocalTime.of(10, 0), ServiceType.BEARD);

        assertThrows(BarberNotAvailableException.class, () -> {
            appointmentService.createAppointment(dto2);
        });
    }

    @Test
    void acceptAppointment_shouldUpdateStatus() {
        var dto = createAppointmentDTO(LocalTime.of(11, 0), ServiceType.BEARD);

        var response = appointmentService.createAppointment(dto);
        var accepted = appointmentService.acceptAppointment(response.id());

        assertEquals(AppointmentStatus.ACCEPTED, accepted.getStatus());
    }

    @Test
    void acceptAppointment_twice_shouldThrowException() {
        var dto = createAppointmentDTO(LocalTime.of(11, 0), ServiceType.HAIRCUT);

        var response = appointmentService.createAppointment(dto);
        appointmentService.acceptAppointment(response.id());

        assertThrows(IllegalStateException.class, () -> {
            appointmentService.acceptAppointment(response.id());
        });
    }

    @Test
    void cancelAppointment_shouldUpdateStatus() {
        var dto = createAppointmentDTO(LocalTime.of(11, 0), ServiceType.BEARD);

        var response = appointmentService.createAppointment(dto);
        var canceled = appointmentService.cancelAppointment(response.id());

        assertEquals(AppointmentStatus.CANCELED, canceled.status());
    }

    @Test
    void getAppointmentByCustomer_shouldReturnAppointments() {
        var dto = createAppointmentDTO(LocalTime.of(11, 0), ServiceType.BEARD);

        appointmentService.createAppointment(dto);

        var appointments = appointmentService.getAppointmentsByCustomer(testCustomer.getId());
        assertFalse(appointments.isEmpty());
        assertTrue(appointments.stream().anyMatch(appt -> appt.serviceType() == ServiceType.BEARD));
    }


}
