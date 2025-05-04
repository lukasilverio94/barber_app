package com.barbershop.exceptions;

import com.barbershop.exceptions.common.NotFoundException;

public class ClientNotFoundException extends NotFoundException {
    public ClientNotFoundException(Long id) {
        super("Client not found with id " + id);
    }
}
