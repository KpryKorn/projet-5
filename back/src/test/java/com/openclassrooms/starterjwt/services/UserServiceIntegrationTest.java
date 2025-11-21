package com.openclassrooms.starterjwt.services;

import com.openclassrooms.starterjwt.models.User;
import com.openclassrooms.starterjwt.repository.UserRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@ActiveProfiles("test")
class UserServiceIntegrationTest {

    private final UserService userService;
    private final UserRepository userRepository;

    @Autowired
    public UserServiceIntegrationTest(UserService userService, UserRepository userRepository) {
        this.userService = userService;
        this.userRepository = userRepository;
    }

    @AfterEach
    void tearDown() {
        userRepository.deleteAll();
    }

    @Test
    void completeUserLifecycle_ShouldCreateFindAndDelete() {
        User user = User.builder()
                .email("integration@test.com")
                .lastName("Integration")
                .firstName("Test")
                .password("password123")
                .admin(false)
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .build();

        User savedUser = userRepository.save(user);
        assertThat(savedUser.getId()).isNotNull();
        assertThat(savedUser.getEmail()).isEqualTo("integration@test.com");

        User foundUser = userService.findById(savedUser.getId());
        assertThat(foundUser).isNotNull();
        assertThat(foundUser.getEmail()).isEqualTo("integration@test.com");
        assertThat(foundUser.getFirstName()).isEqualTo("Test");
        assertThat(foundUser.getLastName()).isEqualTo("Integration");

        userService.delete(savedUser.getId());

        User deletedUser = userService.findById(savedUser.getId());
        assertThat(deletedUser).isNull();
    }
}
