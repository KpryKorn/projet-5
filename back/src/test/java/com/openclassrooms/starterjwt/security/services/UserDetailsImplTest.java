package com.openclassrooms.starterjwt.security.services;

import org.junit.jupiter.api.Test;
import org.springframework.security.core.GrantedAuthority;

import java.util.Collection;

import static org.assertj.core.api.Assertions.assertThat;

class UserDetailsImplTest {

    @Test
    void builder_ShouldCreateUserDetailsWithAllFields() {
        UserDetailsImpl userDetails = UserDetailsImpl.builder()
                .id(1L)
                .username("test@test.com")
                .firstName("John")
                .lastName("Doe")
                .password("password123")
                .admin(true)
                .build();

        assertThat(userDetails.getId()).isEqualTo(1L);
        assertThat(userDetails.getUsername()).isEqualTo("test@test.com");
        assertThat(userDetails.getFirstName()).isEqualTo("John");
        assertThat(userDetails.getLastName()).isEqualTo("Doe");
        assertThat(userDetails.getPassword()).isEqualTo("password123");
        assertThat(userDetails.getAdmin()).isTrue();
    }

    @Test
    void getAuthorities_ShouldReturnEmptyCollection() {
        UserDetailsImpl userDetails = UserDetailsImpl.builder()
                .id(1L)
                .username("test@test.com")
                .firstName("John")
                .lastName("Doe")
                .password("password123")
                .build();

        Collection<? extends GrantedAuthority> authorities = userDetails.getAuthorities();

        assertThat(authorities).isNotNull();
        assertThat(authorities).isEmpty();
    }

    @Test
    void isAccountNonExpired_ShouldReturnTrue() {
        UserDetailsImpl userDetails = UserDetailsImpl.builder()
                .id(1L)
                .username("test@test.com")
                .password("password123")
                .build();

        assertThat(userDetails.isAccountNonExpired()).isTrue();
    }

    @Test
    void isAccountNonLocked_ShouldReturnTrue() {
        UserDetailsImpl userDetails = UserDetailsImpl.builder()
                .id(1L)
                .username("test@test.com")
                .password("password123")
                .build();

        assertThat(userDetails.isAccountNonLocked()).isTrue();
    }

    @Test
    void isCredentialsNonExpired_ShouldReturnTrue() {
        UserDetailsImpl userDetails = UserDetailsImpl.builder()
                .id(1L)
                .username("test@test.com")
                .password("password123")
                .build();

        assertThat(userDetails.isCredentialsNonExpired()).isTrue();
    }

    @Test
    void isEnabled_ShouldReturnTrue() {
        UserDetailsImpl userDetails = UserDetailsImpl.builder()
                .id(1L)
                .username("test@test.com")
                .password("password123")
                .build();

        assertThat(userDetails.isEnabled()).isTrue();
    }

    @Test
    void equals_WithSameId_ShouldReturnTrue() {
        UserDetailsImpl userDetails1 = UserDetailsImpl.builder()
                .id(1L)
                .username("test1@test.com")
                .password("password1")
                .build();

        UserDetailsImpl userDetails2 = UserDetailsImpl.builder()
                .id(1L)
                .username("test2@test.com")
                .password("password2")
                .build();

        assertThat(userDetails1.equals(userDetails2)).isTrue();
    }

    @Test
    void equals_WithDifferentId_ShouldReturnFalse() {
        UserDetailsImpl userDetails1 = UserDetailsImpl.builder()
                .id(1L)
                .username("test@test.com")
                .password("password")
                .build();

        UserDetailsImpl userDetails2 = UserDetailsImpl.builder()
                .id(2L)
                .username("test@test.com")
                .password("password")
                .build();

        assertThat(userDetails1.equals(userDetails2)).isFalse();
    }

    @Test
    void equals_WithSameInstance_ShouldReturnTrue() {
        UserDetailsImpl userDetails = UserDetailsImpl.builder()
                .id(1L)
                .username("test@test.com")
                .password("password")
                .build();

        assertThat(userDetails.equals(userDetails)).isTrue();
    }

    @Test
    void equals_WithNull_ShouldReturnFalse() {
        UserDetailsImpl userDetails = UserDetailsImpl.builder()
                .id(1L)
                .username("test@test.com")
                .password("password")
                .build();

        assertThat(userDetails.equals(null)).isFalse();
    }

    @Test
    void equals_WithDifferentClass_ShouldReturnFalse() {
        UserDetailsImpl userDetails = UserDetailsImpl.builder()
                .id(1L)
                .username("test@test.com")
                .password("password")
                .build();

        String differentObject = "test";

        assertThat(userDetails.equals(differentObject)).isFalse();
    }

    @Test
    void builder_WithAdminFalse_ShouldCreateNonAdminUser() {
        UserDetailsImpl userDetails = UserDetailsImpl.builder()
                .id(2L)
                .username("user@test.com")
                .firstName("Jane")
                .lastName("Smith")
                .password("pass123")
                .admin(false)
                .build();

        assertThat(userDetails.getAdmin()).isFalse();
    }

    @Test
    void builder_WithNullAdmin_ShouldHandleNullValue() {
        UserDetailsImpl userDetails = UserDetailsImpl.builder()
                .id(3L)
                .username("test@test.com")
                .password("password")
                .admin(null)
                .build();

        assertThat(userDetails.getAdmin()).isNull();
    }

    @Test
    void getters_ShouldReturnCorrectValues() {
        UserDetailsImpl userDetails = UserDetailsImpl.builder()
                .id(10L)
                .username("john.doe@test.com")
                .firstName("John")
                .lastName("Doe")
                .password("securePassword")
                .admin(true)
                .build();

        assertThat(userDetails.getId()).isEqualTo(10L);
        assertThat(userDetails.getUsername()).isEqualTo("john.doe@test.com");
        assertThat(userDetails.getFirstName()).isEqualTo("John");
        assertThat(userDetails.getLastName()).isEqualTo("Doe");
        assertThat(userDetails.getPassword()).isEqualTo("securePassword");
        assertThat(userDetails.getAdmin()).isTrue();
    }
}
