package com.barbershop.model;

import jakarta.persistence.*;
import lombok.*;

import java.util.UUID;

@Entity(name = "app_user")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@DiscriminatorColumn(name = "user_type", discriminatorType = DiscriminatorType.STRING)
public class AppUser {

    @Id
    private UUID id;

    @Column(name = "name", nullable = false)
    private String name;

    @Column(name = "phone", unique = true, nullable = false)
    private String phone;

    @Column(name = "email",unique = true, nullable = false)
    private String email;

    @Column(name = "password", nullable = false)
    private String password;

    @PrePersist
    public void ensureId() {
        if (this.id == null) {
            this.id = UUID.randomUUID();
        }
    }

    public String getUserType() {
        DiscriminatorValue val = this.getClass().getAnnotation(DiscriminatorValue.class);
        return val != null ? val.value() : "UNKNOWN";
    }

}
