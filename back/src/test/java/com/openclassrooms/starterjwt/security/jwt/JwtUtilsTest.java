package com.openclassrooms.starterjwt.security.jwt;

import com.openclassrooms.starterjwt.security.services.UserDetailsImpl;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.security.core.Authentication;
import org.springframework.test.util.ReflectionTestUtils;

import java.util.Date;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class JwtUtilsTest {

    private JwtUtils jwtUtils;
    private final String jwtSecret = "testSecretKeyForJwtTokenGenerationAndValidation";
    private final int jwtExpirationMs = 86400000;

    @BeforeEach
    void setUp() {
        jwtUtils = new JwtUtils();
        ReflectionTestUtils.setField(jwtUtils, "jwtSecret", jwtSecret);
        ReflectionTestUtils.setField(jwtUtils, "jwtExpirationMs", jwtExpirationMs);
    }

    @Test
    void generateJwtToken_ShouldReturnValidToken() {
        UserDetailsImpl userDetails = UserDetailsImpl.builder()
                .id(1L)
                .username("test@test.com")
                .firstName("Test")
                .lastName("User")
                .password("password")
                .build();

        Authentication authentication = mock(Authentication.class);
        when(authentication.getPrincipal()).thenReturn(userDetails);

        String token = jwtUtils.generateJwtToken(authentication);

        assertThat(token).isNotNull();
        assertThat(token).isNotEmpty();
    }

    @Test
    void getUserNameFromJwtToken_ShouldReturnUsername() {
        String username = "test@test.com";
        String token = Jwts.builder()
                .setSubject(username)
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + jwtExpirationMs))
                .signWith(SignatureAlgorithm.HS512, jwtSecret)
                .compact();

        String extractedUsername = jwtUtils.getUserNameFromJwtToken(token);

        assertThat(extractedUsername).isEqualTo(username);
    }

    @Test
    void validateJwtToken_WithValidToken_ShouldReturnTrue() {
        String token = Jwts.builder()
                .setSubject("test@test.com")
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + jwtExpirationMs))
                .signWith(SignatureAlgorithm.HS512, jwtSecret)
                .compact();

        boolean isValid = jwtUtils.validateJwtToken(token);

        assertThat(isValid).isTrue();
    }

    @Test
    void validateJwtToken_WithInvalidSignature_ShouldReturnFalse() {
        String token = Jwts.builder()
                .setSubject("test@test.com")
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + jwtExpirationMs))
                .signWith(SignatureAlgorithm.HS512, "wrongSecret")
                .compact();

        boolean isValid = jwtUtils.validateJwtToken(token);

        assertThat(isValid).isFalse();
    }

    @Test
    void validateJwtToken_WithMalformedToken_ShouldReturnFalse() {
        String malformedToken = "malformed.jwt.token";

        boolean isValid = jwtUtils.validateJwtToken(malformedToken);

        assertThat(isValid).isFalse();
    }

    @Test
    void validateJwtToken_WithExpiredToken_ShouldReturnFalse() {
        String expiredToken = Jwts.builder()
                .setSubject("test@test.com")
                .setIssuedAt(new Date(System.currentTimeMillis() - 10000))
                .setExpiration(new Date(System.currentTimeMillis() - 5000))
                .signWith(SignatureAlgorithm.HS512, jwtSecret)
                .compact();

        boolean isValid = jwtUtils.validateJwtToken(expiredToken);

        assertThat(isValid).isFalse();
    }

    @Test
    void validateJwtToken_WithEmptyToken_ShouldReturnFalse() {
        boolean isValid = jwtUtils.validateJwtToken("");

        assertThat(isValid).isFalse();
    }

    @Test
    void validateJwtToken_WithNullToken_ShouldReturnFalse() {
        boolean isValid = jwtUtils.validateJwtToken(null);

        assertThat(isValid).isFalse();
    }
}
