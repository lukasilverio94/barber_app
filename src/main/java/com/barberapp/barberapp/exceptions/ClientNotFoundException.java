package com.barberapp.barberapp.exceptions;

import com.barberapp.barberapp.exceptions.common.NotFoundException;

public class ClientNotFoundException extends NotFoundException {
    public ClientNotFoundException(Long id) {
        super("Client not found with id " + id);
    }
}
