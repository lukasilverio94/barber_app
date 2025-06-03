package com.barbershop.factory;

import com.barbershop.model.Customer;

import java.util.UUID;

public class CustomerFactory {

    public static Customer createDefaultCustomer() {
        return createCustomer("John Doe", "john@doe.com", "+123456789");
    }

    public static Customer createCustomer(String name, String email, String phone) {
        Customer customer = new Customer();
        customer.setId(UUID.randomUUID());
        customer.setName(name);
        customer.setEmail(email);
        customer.setPhone(phone);
        customer.setPassword("password123");
        return customer;
    }
}
