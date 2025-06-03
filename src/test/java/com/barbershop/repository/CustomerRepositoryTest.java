package com.barbershop.repository;

import com.barbershop.factory.CustomerFactory;
import com.barbershop.model.Customer;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.jdbc.EmbeddedDatabaseConnection;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.List;
import java.util.Optional;

@DataJpaTest
@AutoConfigureTestDatabase(connection = EmbeddedDatabaseConnection.H2)
public class CustomerRepositoryTest {

    @Autowired
    private CustomerRepository customerRepository;

    @Test
    public void CustomerRepository_SaveCustomer_ReturnsSavedCustomer() {
        Customer customer = CustomerFactory.createDefaultCustomer();

        Customer saved = customerRepository.save(customer);

        Assertions.assertThat(saved).isNotNull();
        Assertions.assertThat(saved.getId()).isNotNull();
    }

    @Test
    public void CustomerRepository_GetAll_ReturnsCustomerList() {
        Customer c1 = CustomerFactory.createCustomer("Alice", "alice@test.com", "+111");
        Customer c2 = CustomerFactory.createCustomer("Bob", "bob@test.com", "+222");

        customerRepository.save(c1);
        customerRepository.save(c2);

        List<Customer> customers = customerRepository.findAll();

        Assertions.assertThat(customers).hasSizeGreaterThanOrEqualTo(2);
    }

    @Test
    public void CustomerRepository_FindById_ReturnsCustomer() {
        Customer customer = CustomerFactory.createDefaultCustomer();
        customerRepository.save(customer);

        Optional<Customer> result = customerRepository.findById(customer.getId());

        Assertions.assertThat(result).isPresent();
        Assertions.assertThat(result.get().getEmail()).isEqualTo(customer.getEmail());
    }

    @Test
    public void CustomerRepository_ExistsByEmail_ReturnsTrue() {
        Customer customer = CustomerFactory.createCustomer("Emma", "emma@test.com", "+333");
        customerRepository.save(customer);

        boolean exists = customerRepository.existsByEmail("emma@test.com");

        Assertions.assertThat(exists).isTrue();
    }

    @Test
    public void CustomerRepository_UpdateCustomer_ReturnsUpdatedCustomer() {
        Customer customer = CustomerFactory.createDefaultCustomer();
        customerRepository.save(customer);

        Customer toUpdate = customerRepository.findById(customer.getId()).get();
        toUpdate.setName("Updated Name");
        toUpdate.setEmail("updated@test.com");
        toUpdate.setPhone("+444");

        Customer updated = customerRepository.save(toUpdate);

        Assertions.assertThat(updated.getName()).isEqualTo("Updated Name");
        Assertions.assertThat(updated.getEmail()).isEqualTo("updated@test.com");
        Assertions.assertThat(updated.getPhone()).isEqualTo("+444");
    }

    @Test
    public void CustomerRepository_DeleteCustomer_ReturnsEmpty() {
        Customer customer = CustomerFactory.createDefaultCustomer();
        customerRepository.save(customer);

        customerRepository.deleteById(customer.getId());

        Optional<Customer> result = customerRepository.findById(customer.getId());

        Assertions.assertThat(result).isEmpty();
    }
}
