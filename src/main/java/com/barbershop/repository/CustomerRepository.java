package com.barbershop.repository;

import com.barbershop.model.Customer;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface CustomerRepository extends JpaRepository<Customer, UUID> {

    boolean existsByEmail(String email);

    Customer findCustomerByEmail(String email);
}
