package com.barbershop.service;

import com.barbershop.dto.CustomerCreateDTO;
import com.barbershop.dto.CustomerDTO;
import com.barbershop.dto.mappers.CustomerMapper;
import com.barbershop.exception.EmailAlreadyExistsException;
import com.barbershop.model.Customer;
import com.barbershop.repository.CustomerRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CustomerService {

    private final CustomerRepository customerRepository;
    private final PasswordEncoder passwordEncoder;

    public CustomerService(CustomerRepository customerRepository, PasswordEncoder passwordEncoder) {
        this.customerRepository = customerRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public List<CustomerDTO> getAllCustomers() {
        return customerRepository.findAll()
                .stream().map(CustomerMapper::toDto)
                .toList();
    }

    public CustomerDTO createCustomer(CustomerCreateDTO dto) {
        if(customerRepository.existsByEmail(dto.email())) {
            throw new EmailAlreadyExistsException("Email already in use");
        }

        Customer customer = CustomerMapper.toEntity(dto);
        customer.setPassword(passwordEncoder.encode(dto.password()));
        Customer savedCustomer = customerRepository.save(customer);
        return CustomerMapper.toDto(savedCustomer);
    }

}
