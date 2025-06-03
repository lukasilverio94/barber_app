package com.barbershop.factory;

import com.barbershop.model.AppUser;

import java.util.UUID;

public class AppUserFactory {

    public static AppUser createDefaultUser() {
        return AppUser.builder()
                .id(UUID.randomUUID())
                .name("testuser")
                .phone("+9999999999")
                .email("test@test.com")
                .password("testpass")
                .build();
    }

    public static AppUser createUser(String name, String phone, String email) {
        return AppUser.builder()
                .id(UUID.randomUUID())
                .name(name)
                .phone(phone)
                .email(email)
                .password("pass")
                .build();
    }
}
