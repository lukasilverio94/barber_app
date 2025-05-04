package com.barberapp.barberapp.repositories;

import com.barberapp.barberapp.models.Client;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ClientRepository extends JpaRepository<Client, Long> {

}
