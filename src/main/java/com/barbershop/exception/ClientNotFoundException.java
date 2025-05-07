package com.barbershop.exception;

import com.barbershop.exception.common.NotFoundException;

public class ClientNotFoundException extends NotFoundException {
    public ClientNotFoundException(Long id) {
        super("Client not found with id " + id);
    }
}
