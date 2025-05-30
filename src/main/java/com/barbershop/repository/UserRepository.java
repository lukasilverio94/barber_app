package com.barbershop.repository;

import com.barbershop.model.AppUser;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface UserRepository extends JpaRepository<AppUser, UUID> {
}
