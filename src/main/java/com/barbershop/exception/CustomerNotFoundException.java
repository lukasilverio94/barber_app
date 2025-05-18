package com.barbershop.exception;

import com.barbershop.exception.common.NotFoundException;

import java.util.UUID;

public class CustomerNotFoundException extends NotFoundException {
    public CustomerNotFoundException(UUID id) {
        super("Client not found with id " + id);
    }
}
