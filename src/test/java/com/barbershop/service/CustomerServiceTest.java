package com.barbershop.service;

import com.barbershop.dto.CustomerCreateDTO;
import com.barbershop.dto.CustomerDTO;
import com.barbershop.exception.EmailAlreadyExistsException;
import com.barbershop.factory.CustomerFactory;
import com.barbershop.model.Customer;
import com.barbershop.repository.CustomerRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CustomerServiceTest {

    @Mock
    private CustomerRepository customerRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @InjectMocks
    private CustomerService customerService;

    @Test
    void getAllCustomers_ShouldReturnListOfCustomerDTOs() {
        // Arrange
        Customer customer1 = CustomerFactory.createCustomer("test1", "test1@test.com", "+111");
        Customer customer2 = CustomerFactory.createCustomer("test2", "test2@test.com", "+222");
        List<Customer> customers = List.of(customer1, customer2);

        when(customerRepository.findAll()).thenReturn(customers);

        // Act
        List<CustomerDTO> result = customerService.getAllCustomers();

        // Assert
        assertEquals(2, result.size());
        assertEquals("test1", result.get(0).name());
        assertEquals("test1@test.com", result.get(0).email());
        assertEquals("test2", result.get(1).name());
        assertEquals("test2@test.com", result.get(1).email());
        verify(customerRepository, times(1)).findAll();
    }

    @Test
    void createCustomer_WithValidData_ShouldReturnCreatedCustomer() {
        // Arrange
        CustomerCreateDTO createDTO = new CustomerCreateDTO(
                "New Customer",
                "new@customer.com",
                "+123456789",
                "password123"
        );

        Customer savedCustomer = CustomerFactory.createCustomer(
                createDTO.name(),
                createDTO.email(),
                createDTO.phone()
        );

        when(customerRepository.existsByEmail(createDTO.email())).thenReturn(false);
        when(passwordEncoder.encode(createDTO.password())).thenReturn("encodedPassword");
        when(customerRepository.save(any(Customer.class))).thenReturn(savedCustomer);

        // Act
        CustomerDTO result = customerService.createCustomer(createDTO);

        // Assert
        assertNotNull(result.id());
        assertEquals(createDTO.name(), result.name());
        assertEquals(createDTO.email(), result.email());
        assertEquals(createDTO.phone(), result.phone());

        verify(customerRepository).existsByEmail(createDTO.email());
        verify(passwordEncoder).encode(createDTO.password());
        verify(customerRepository).save(argThat(customer ->
                customer.getPassword().equals("encodedPassword")));
    }

    @Test
    void createCustomer_WithExistingEmail_ShouldThrowException() {
        // Arrange
        CustomerCreateDTO createDTO = new CustomerCreateDTO(
                "Existing Customer",
                "existing@customer.com",
                "+987654321",
                "password123"
        );

        when(customerRepository.existsByEmail(createDTO.email())).thenReturn(true);

        // Act & Assert
        assertThrows(EmailAlreadyExistsException.class, () -> {
            customerService.createCustomer(createDTO);
        });

        verify(customerRepository).existsByEmail(createDTO.email());
        verify(customerRepository, never()).save(any());
        verify(passwordEncoder, never()).encode(any());
    }

//    @Test
//    void createCustomer_ShouldGenerateIdIfNotProvided() {
//        // Arrange
//        CustomerCreateDTO createDTO = new CustomerCreateDTO(
//                "name",
//                "+000000000",
//                "email@test.com",
//                "password123"
//        );
//
//        Customer savedCustomer = CustomerFactory.createCustomer(
//                createDTO.name(),
//                createDTO.email(),
//                createDTO.phone()
//        );
//
//        when(customerRepository.existsByEmail(createDTO.email())).thenReturn(false);
//        when(passwordEncoder.encode(createDTO.password())).thenReturn("encodedPassword");
//        when(customerRepository.save(any(Customer.class))).thenReturn(savedCustomer);
//
//        // Act
//        CustomerDTO result = customerService.createCustomer(createDTO);
//
//        System.out.println("RESULT = " + result);
//        // Assert
//        assertNotNull(result.id());
//        verify(customerRepository).save(argThat(customer ->
//                customer.getId() != null &&
//                        customer.getEmail().equals(createDTO.email()) &&
//                        customer.getName().equals(createDTO.name())
//        ));
//    }

    @Test
    void createCustomer_ShouldEncodePassword() {
        // Arrange
        CustomerCreateDTO createDTO = new CustomerCreateDTO(
                "Password Test",
                "password@test.com",
                "+111222333",
                "rawPassword"
        );

        Customer savedCustomer = CustomerFactory.createCustomer(
                createDTO.name(),
                createDTO.email(),
                createDTO.phone()
        );

        when(customerRepository.existsByEmail(createDTO.email())).thenReturn(false);
        when(passwordEncoder.encode(createDTO.password())).thenReturn("encodedPassword");
        when(customerRepository.save(any(Customer.class))).thenReturn(savedCustomer);

        // Act
        customerService.createCustomer(createDTO);

        // Assert
        verify(passwordEncoder).encode("rawPassword");
        verify(customerRepository).save(argThat(customer ->
                customer.getPassword().equals("encodedPassword")));
    }
}