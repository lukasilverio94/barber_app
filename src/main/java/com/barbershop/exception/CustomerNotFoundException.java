package com.barbershop.exception;

import com.barbershop.exception.common.NotFoundException;

import java.util.UUID;

public class CustomerNotFoundException extends NotFoundException {
    private static final String CODE = "CUST-001";

    public CustomerNotFoundException(UUID id) {
        super("Customer not found with id " + id, CODE);
    }
}