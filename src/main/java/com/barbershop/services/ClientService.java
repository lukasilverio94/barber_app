package com.barbershop.services;

import com.barbershop.exceptions.ClientNotFoundException;
import com.barbershop.models.Client;
import com.barbershop.repositories.ClientRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ClientService {

    private final ClientRepository clientRepository;

    public List<Client> getAllClients() {
        return clientRepository.findAll();
    }

    public Client getClientByIdOrThrow(Long id) {
        return clientRepository.findById(id)
                .orElseThrow(() -> new ClientNotFoundException(id));
    }

    public Client createClient(Client client) {
        return clientRepository.save(client);
    }

    public Client updateClient(Long id, Client updatedClient) {
        Client existing = getClientByIdOrThrow(id);
        existing.setName(updatedClient.getName());
        existing.setPhone(updatedClient.getPhone());
        existing.setEmail(updatedClient.getEmail());
        return clientRepository.save(existing);
    }

    public void deleteClient(Long id) {
        Client client = getClientByIdOrThrow(id);
        clientRepository.delete(client);
    }
}
