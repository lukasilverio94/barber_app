package com.barbershop.repository;

import com.barbershop.model.AppUser;
import com.barbershop.model.Barber;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;
import java.util.UUID;

public interface AppUserRepository extends JpaRepository<AppUser, UUID> {

    Optional<AppUser> findByEmail(String email);

    @Query("SELECT u FROM app_user u WHERE u.id = :id AND TYPE(u) = Barber")
    Optional<Barber> findBarberById(@Param("id") UUID id);
}
