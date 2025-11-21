package com.openclassrooms.starterjwt.security.services;

import com.openclassrooms.starterjwt.models.User;
import com.openclassrooms.starterjwt.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class UserDetailsServiceImplTest {

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private UserDetailsServiceImpl userDetailsService;

    @Test
    void loadUserByUsername_WithValidEmail_ShouldReturnUserDetails() {
        User user = User.builder()
                .id(1L)
                .email("test@test.com")
                .firstName("John")
                .lastName("Doe")
                .password("password123")
                .admin(false)
                .build();

        when(userRepository.findByEmail("test@test.com")).thenReturn(Optional.of(user));

        UserDetails userDetails = userDetailsService.loadUserByUsername("test@test.com");

        assertThat(userDetails).isNotNull();
        assertThat(userDetails.getUsername()).isEqualTo("test@test.com");
        assertThat(userDetails.getPassword()).isEqualTo("password123");
        verify(userRepository).findByEmail("test@test.com");
    }

    @Test
    void loadUserByUsername_WithInvalidEmail_ShouldThrowException() {
        when(userRepository.findByEmail(anyString())).thenReturn(Optional.empty());

        assertThatThrownBy(() -> userDetailsService.loadUserByUsername("invalid@test.com"))
                .isInstanceOf(UsernameNotFoundException.class)
                .hasMessageContaining("User Not Found with email: invalid@test.com");

        verify(userRepository).findByEmail("invalid@test.com");
    }

    @Test
    void loadUserByUsername_WithAdminUser_ShouldReturnUserDetails() {
        User adminUser = User.builder()
                .id(2L)
                .email("admin@test.com")
                .firstName("Admin")
                .lastName("User")
                .password("adminpass")
                .admin(true)
                .build();

        when(userRepository.findByEmail("admin@test.com")).thenReturn(Optional.of(adminUser));

        UserDetails userDetails = userDetailsService.loadUserByUsername("admin@test.com");

        assertThat(userDetails).isNotNull();
        assertThat(userDetails.getUsername()).isEqualTo("admin@test.com");
        assertThat(userDetails.getPassword()).isEqualTo("adminpass");
    }

    @Test
    void loadUserByUsername_ShouldMapAllUserFields() {
        User user = User.builder()
                .id(3L)
                .email("user@example.com")
                .firstName("Jane")
                .lastName("Smith")
                .password("securepass")
                .admin(false)
                .build();

        when(userRepository.findByEmail("user@example.com")).thenReturn(Optional.of(user));

        UserDetails userDetails = userDetailsService.loadUserByUsername("user@example.com");
        UserDetailsImpl userDetailsImpl = (UserDetailsImpl) userDetails;

        assertThat(userDetailsImpl.getId()).isEqualTo(3L);
        assertThat(userDetailsImpl.getUsername()).isEqualTo("user@example.com");
        assertThat(userDetailsImpl.getFirstName()).isEqualTo("Jane");
        assertThat(userDetailsImpl.getLastName()).isEqualTo("Smith");
        assertThat(userDetailsImpl.getPassword()).isEqualTo("securepass");
    }

    @Test
    void loadUserByUsername_WithNonExistentUser_ShouldThrowException() {
        when(userRepository.findByEmail("nonexistent@test.com")).thenReturn(Optional.empty());

        assertThatThrownBy(() -> userDetailsService.loadUserByUsername("nonexistent@test.com"))
                .isInstanceOf(UsernameNotFoundException.class);
    }
}
