package com.barbershop.service;

import com.barbershop.BarberappApplication;
import com.barbershop.dto.AppointmentCreateDTO;
import com.barbershop.dto.AppointmentResponseDTO;
import com.barbershop.enums.ServiceType;
import com.barbershop.model.Barber;
import com.barbershop.model.Customer;
import com.barbershop.repository.BarberRepository;
import com.barbershop.repository.CustomerRepository;
import com.barbershop.utils.PostgresContainerTest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDate;
import java.time.LocalTime;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

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

        testCustomer = customerRepository.saveAndFlush(testCustomer);

        assertNotNull(testCustomer.getId());
    }


    @Test
    void createAppointment_shouldPersistSuccessfully() {
        var dto = new AppointmentCreateDTO(
                testBarber.getId(),
                testCustomer.getId(),
                LocalDate.now().plusDays(1),
                LocalTime.of(10, 0),
                ServiceType.HAIRCUT
        );

        AppointmentResponseDTO response = appointmentService.createAppointment(dto);

        assertNotNull(response);
        assertEquals(testBarber.getId(), response.barberId());
        assertEquals(ServiceType.HAIRCUT, response.serviceType());

        assertEquals(dto.date(), response.day());
        assertEquals(dto.startTime(), response.startTime());

    }
}
