package com.barbershop.repository;

import com.barbershop.factory.AppUserFactory;
import com.barbershop.factory.BarberFactory;
import com.barbershop.utils.PostgresContainerTest;
import com.barbershop.model.AppUser;
import com.barbershop.model.Barber;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;
import java.util.Optional;

@SpringBootTest
public class AppUserRepositoryTest extends PostgresContainerTest {

    @Autowired
    private AppUserRepository userRepository;

    @Test
    public void AppUserRepository_SaveAll_ReturnsSavedUser() {
        // Arrange
        AppUser user = AppUserFactory.createDefaultUser();
        // Act
        AppUser savedUser = userRepository.save(user);
        // Assert
        Assertions.assertThat(savedUser).isNotNull();
    }

    @Test
    public void AppUserRepository_GetAll_ReturnsMoreThanOneUser() {
        AppUser user1 = AppUserFactory.createUser("user1", "+111", "user1@test.com");
        AppUser user2 = AppUserFactory.createUser("user2", "+222", "user2@test.com");

        userRepository.save(user1);
        userRepository.save(user2);

        List<AppUser> usersList = userRepository.findAll();

        Assertions.assertThat(usersList).isNotEmpty();
        Assertions.assertThat(usersList.size()).isGreaterThan(1);
    }

    @Test
    public void AppUserRepository_FindById_ReturnUser() {
        AppUser user = AppUserFactory.createDefaultUser();
        userRepository.save(user);
        Optional<AppUser> foundUser = userRepository.findById(user.getId());
        Assertions.assertThat(foundUser).isNotNull();

    }

    @Test
    public void AppUserRepository_FindBarberById_ReturnsBarber() {
        // arrange
        Barber barber = BarberFactory.createDefaultBarber();
        userRepository.save(barber);

        // act
        Optional<Barber> foundBarber = userRepository.findBarberById(barber.getId());

        // assert
        Assertions.assertThat(foundBarber).isPresent();
        Assertions.assertThat(foundBarber.get()).isInstanceOf(Barber.class);
        Assertions.assertThat(foundBarber.get().getName()).isEqualTo(barber.getName());

    }

    @Test
    public void AppUserRepository_UpdateUser_ReturnsUserNotNull() {
        // arrange
        AppUser user = AppUserFactory.createDefaultUser();
        userRepository.save(user);

        // act
        AppUser saveUser = userRepository.findById(user.getId()).get();

        saveUser.setName("NewTestName");
        saveUser.setEmail("newemail@test.com");
        saveUser.setPhone("+333");
        saveUser.setPassword("newpass");

        AppUser updatedUser = userRepository.save(saveUser);
        // assert
        Assertions.assertThat(updatedUser).isNotNull();
        Assertions.assertThat(updatedUser.getName()).isEqualTo("NewTestName");
        Assertions.assertThat(updatedUser.getEmail()).isEqualTo("newemail@test.com");
        Assertions.assertThat(updatedUser.getPhone()).isEqualTo("+333");
        Assertions.assertThat(updatedUser.getPassword()).isEqualTo("newpass");

    }

    @Test
    public void AppUserRepository_DeleteUser_ReturnUserIsEmpty() {
        // arrange
        AppUser user = AppUserFactory.createDefaultUser();
        userRepository.save(user);

        // act
        userRepository.deleteById(user.getId());
        Optional<AppUser> userReturned = userRepository.findById(user.getId());
        // assert
        Assertions.assertThat(userReturned).isEmpty();

    }
}
