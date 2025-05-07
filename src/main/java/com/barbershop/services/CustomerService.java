package com.barbershop.services;

import com.barbershop.exceptions.ClientNotFoundException;
import com.barbershop.models.Customer;
import com.barbershop.repositories.CustomerRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CustomerService {

    private final CustomerRepository customerRepository;

    public List<Customer> getAllClients() {
        return customerRepository.findAll();
    }

    public Customer getCustomerByIdOrThrow(Long id) {
        return customerRepository.findById(id)
                .orElseThrow(() -> new ClientNotFoundException(id));
    }

    public Customer createCustomer(Customer customer) {
        return customerRepository.save(customer);
    }

    public Customer updateCustomer(Long id, Customer updatedCustomer) {
        Customer existing = getCustomerByIdOrThrow(id);
        existing.setName(updatedCustomer.getName());
        existing.setPhone(updatedCustomer.getPhone());
        existing.setEmail(updatedCustomer.getEmail());
        return customerRepository.save(existing);
    }

    public void deleteCustomer(Long id) {
        Customer customer = getCustomerByIdOrThrow(id);
        customerRepository.delete(customer);
    }
}
