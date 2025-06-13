package com.barbershop.service;

import com.barbershop.dto.AppointmentCreateDTO;
import com.barbershop.enums.ServiceType;
import com.barbershop.exception.OutsideBusinessHoursException;
import com.barbershop.model.Appointment;
import com.barbershop.model.Barber;
import com.barbershop.model.Customer;
import com.barbershop.repository.AppUserRepository;
import com.barbershop.repository.AppointmentRepository;
import com.barbershop.repository.CustomerRepository;
import com.barbershop.validation.AppointmentValidator;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class AppointmentServiceTest {

    @Mock
    AppointmentRepository appointmentRepository;
    @Mock
    CustomerRepository customerRepository;
    @Mock
    AppUserRepository appUserRepository;
    @Mock
    AppointmentValidator appointmentValidator;
    @Mock
    NotificationService notificationService;

    @InjectMocks
    AppointmentService appointmentService;

    @Captor
    ArgumentCaptor<Appointment> appointment;


    @Test
    void createAppointmentShouldCreateSuccessfully() {
        var dto = new AppointmentCreateDTO(UUID.randomUUID(), UUID.randomUUID(), LocalDate.now(), LocalTime.now(), ServiceType.HAIRCUT);

        var customer = new Customer();
        var barber = new Barber();
        barber.setId(dto.barberId());

        var appointment = new Appointment();
        appointment.setBarber(barber);
        appointment.setCustomer(customer);
        appointment.setStartTime(dto.startTime());
        appointment.setApptDay(dto.date());
        appointment.setServiceType(dto.serviceType());

        when(this.customerRepository.findById(dto.customerId())).thenReturn(Optional.of(customer));
        when(this.appUserRepository.findBarberById(dto.barberId())).thenReturn(Optional.of(barber));
        when(this.appointmentValidator.isWhithinBusinessHours(dto.startTime(), dto.date())).thenReturn(true);
        doNothing().when(this.appointmentValidator).validateBarberAvailability(dto.barberId(), dto.date(), dto.startTime());
        when(this.appointmentRepository.save(any(Appointment.class))).thenReturn(appointment);

        var createdAppointment = this.appointmentService.createAppointment(dto);

        // capture the saved appointment
        verify(appointmentRepository).save(this.appointment.capture());

        var savedAppointment = this.appointment.getValue();

        assertEquals(dto.barberId(), createdAppointment.barberId());
        assertEquals(dto.barberId(), savedAppointment.getBarber().getId());

    }

    @Test
    void createAppointmentShouldThrowWhenOutsideBusinessHours() {
        var dto = new AppointmentCreateDTO(UUID.randomUUID(), UUID.randomUUID(), LocalDate.now(), LocalTime.of(23, 0), ServiceType.HAIRCUT);

        when(customerRepository.findById(dto.customerId())).thenReturn(Optional.of(new Customer()));
        when(appUserRepository.findBarberById(dto.barberId())).thenReturn(Optional.of(new Barber()));
        when(appointmentValidator.isWhithinBusinessHours(dto.startTime(), dto.date())).thenReturn(false);

        assertThrows(OutsideBusinessHoursException.class, () -> appointmentService.createAppointment(dto));
    }

}
