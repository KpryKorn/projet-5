package com.openclassrooms.starterjwt.payload.request;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("SignupRequest Tests")
class SignupRequestTest {

    private Validator validator;

    @BeforeEach
    void setUp() {
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        validator = factory.getValidator();
    }

    @Test
    @DisplayName("Valid signup request should pass validation")
    void validSignupRequest_ShouldPassValidation() {
        SignupRequest request = new SignupRequest();
        request.setEmail("test@test.com");
        request.setFirstName("John");
        request.setLastName("Doe");
        request.setPassword("password123");

        Set<ConstraintViolation<SignupRequest>> violations = validator.validate(request);

        assertThat(violations).isEmpty();
    }

    @Test
    @DisplayName("Getters should return correct values")
    void getters_ShouldReturnCorrectValues() {
        SignupRequest request = new SignupRequest();
        request.setEmail("user@test.com");
        request.setFirstName("Jane");
        request.setLastName("Smith");
        request.setPassword("securePass");

        assertThat(request.getEmail()).isEqualTo("user@test.com");
        assertThat(request.getFirstName()).isEqualTo("Jane");
        assertThat(request.getLastName()).isEqualTo("Smith");
        assertThat(request.getPassword()).isEqualTo("securePass");
    }

    @Test
    @DisplayName("Email cannot be null")
    void email_CannotBeNull() {
        SignupRequest request = new SignupRequest();
        request.setEmail(null);
        request.setFirstName("John");
        request.setLastName("Doe");
        request.setPassword("password123");

        Set<ConstraintViolation<SignupRequest>> violations = validator.validate(request);

        assertThat(violations).isNotEmpty();
        assertThat(violations).anyMatch(v -> v.getPropertyPath().toString().equals("email"));
    }

    @Test
    @DisplayName("Email cannot be empty")
    void email_CannotBeEmpty() {
        SignupRequest request = new SignupRequest();
        request.setEmail("");
        request.setFirstName("John");
        request.setLastName("Doe");
        request.setPassword("password123");

        Set<ConstraintViolation<SignupRequest>> violations = validator.validate(request);

        assertThat(violations).isNotEmpty();
        assertThat(violations).anyMatch(v -> v.getPropertyPath().toString().equals("email"));
    }

    @Test
    @DisplayName("Email cannot be blank")
    void email_CannotBeBlank() {
        SignupRequest request = new SignupRequest();
        request.setEmail("   ");
        request.setFirstName("John");
        request.setLastName("Doe");
        request.setPassword("password123");

        Set<ConstraintViolation<SignupRequest>> violations = validator.validate(request);

        assertThat(violations).isNotEmpty();
        assertThat(violations).anyMatch(v -> v.getPropertyPath().toString().equals("email"));
    }

    @Test
    @DisplayName("Email must be valid format")
    void email_MustBeValidFormat() {
        SignupRequest request = new SignupRequest();
        request.setEmail("invalid-email");
        request.setFirstName("John");
        request.setLastName("Doe");
        request.setPassword("password123");

        Set<ConstraintViolation<SignupRequest>> violations = validator.validate(request);

        assertThat(violations).isNotEmpty();
        assertThat(violations).anyMatch(v -> v.getPropertyPath().toString().equals("email"));
    }

    @Test
    @DisplayName("Email with max size should be valid")
    void email_WithMaxSize_ShouldBeValid() {
        String maxEmail = "a".repeat(38) + "@test.com";
        SignupRequest request = new SignupRequest();
        request.setEmail(maxEmail);
        request.setFirstName("John");
        request.setLastName("Doe");
        request.setPassword("password123");

        Set<ConstraintViolation<SignupRequest>> violations = validator.validate(request);

        assertThat(violations).isEmpty();
    }

    @Test
    @DisplayName("Email exceeding max size should be invalid")
    void email_ExceedingMaxSize_ShouldBeInvalid() {
        String tooLongEmail = "a".repeat(50) + "@test.com";
        SignupRequest request = new SignupRequest();
        request.setEmail(tooLongEmail);
        request.setFirstName("John");
        request.setLastName("Doe");
        request.setPassword("password123");

        Set<ConstraintViolation<SignupRequest>> violations = validator.validate(request);

        assertThat(violations).isNotEmpty();
        assertThat(violations).anyMatch(v -> v.getPropertyPath().toString().equals("email"));
    }

    @Test
    @DisplayName("FirstName cannot be null")
    void firstName_CannotBeNull() {
        SignupRequest request = new SignupRequest();
        request.setEmail("test@test.com");
        request.setFirstName(null);
        request.setLastName("Doe");
        request.setPassword("password123");

        Set<ConstraintViolation<SignupRequest>> violations = validator.validate(request);

        assertThat(violations).isNotEmpty();
        assertThat(violations).anyMatch(v -> v.getPropertyPath().toString().equals("firstName"));
    }

    @Test
    @DisplayName("FirstName cannot be empty")
    void firstName_CannotBeEmpty() {
        SignupRequest request = new SignupRequest();
        request.setEmail("test@test.com");
        request.setFirstName("");
        request.setLastName("Doe");
        request.setPassword("password123");

        Set<ConstraintViolation<SignupRequest>> violations = validator.validate(request);

        assertThat(violations).isNotEmpty();
        assertThat(violations).anyMatch(v -> v.getPropertyPath().toString().equals("firstName"));
    }

    @Test
    @DisplayName("FirstName with min size should be valid")
    void firstName_WithMinSize_ShouldBeValid() {
        SignupRequest request = new SignupRequest();
        request.setEmail("test@test.com");
        request.setFirstName("Joe");
        request.setLastName("Doe");
        request.setPassword("password123");

        Set<ConstraintViolation<SignupRequest>> violations = validator.validate(request);

        assertThat(violations).isEmpty();
    }

    @Test
    @DisplayName("FirstName below min size should be invalid")
    void firstName_BelowMinSize_ShouldBeInvalid() {
        SignupRequest request = new SignupRequest();
        request.setEmail("test@test.com");
        request.setFirstName("Jo");
        request.setLastName("Doe");
        request.setPassword("password123");

        Set<ConstraintViolation<SignupRequest>> violations = validator.validate(request);

        assertThat(violations).isNotEmpty();
        assertThat(violations).anyMatch(v -> v.getPropertyPath().toString().equals("firstName"));
    }

    @Test
    @DisplayName("FirstName with max size should be valid")
    void firstName_WithMaxSize_ShouldBeValid() {
        SignupRequest request = new SignupRequest();
        request.setEmail("test@test.com");
        request.setFirstName("J".repeat(20));
        request.setLastName("Doe");
        request.setPassword("password123");

        Set<ConstraintViolation<SignupRequest>> violations = validator.validate(request);

        assertThat(violations).isEmpty();
    }

    @Test
    @DisplayName("FirstName exceeding max size should be invalid")
    void firstName_ExceedingMaxSize_ShouldBeInvalid() {
        SignupRequest request = new SignupRequest();
        request.setEmail("test@test.com");
        request.setFirstName("J".repeat(21));
        request.setLastName("Doe");
        request.setPassword("password123");

        Set<ConstraintViolation<SignupRequest>> violations = validator.validate(request);

        assertThat(violations).isNotEmpty();
        assertThat(violations).anyMatch(v -> v.getPropertyPath().toString().equals("firstName"));
    }

    @Test
    @DisplayName("LastName cannot be null")
    void lastName_CannotBeNull() {
        SignupRequest request = new SignupRequest();
        request.setEmail("test@test.com");
        request.setFirstName("John");
        request.setLastName(null);
        request.setPassword("password123");

        Set<ConstraintViolation<SignupRequest>> violations = validator.validate(request);

        assertThat(violations).isNotEmpty();
        assertThat(violations).anyMatch(v -> v.getPropertyPath().toString().equals("lastName"));
    }

    @Test
    @DisplayName("LastName cannot be empty")
    void lastName_CannotBeEmpty() {
        SignupRequest request = new SignupRequest();
        request.setEmail("test@test.com");
        request.setFirstName("John");
        request.setLastName("");
        request.setPassword("password123");

        Set<ConstraintViolation<SignupRequest>> violations = validator.validate(request);

        assertThat(violations).isNotEmpty();
        assertThat(violations).anyMatch(v -> v.getPropertyPath().toString().equals("lastName"));
    }

    @Test
    @DisplayName("LastName with min size should be valid")
    void lastName_WithMinSize_ShouldBeValid() {
        SignupRequest request = new SignupRequest();
        request.setEmail("test@test.com");
        request.setFirstName("John");
        request.setLastName("Doe");
        request.setPassword("password123");

        Set<ConstraintViolation<SignupRequest>> violations = validator.validate(request);

        assertThat(violations).isEmpty();
    }

    @Test
    @DisplayName("LastName below min size should be invalid")
    void lastName_BelowMinSize_ShouldBeInvalid() {
        SignupRequest request = new SignupRequest();
        request.setEmail("test@test.com");
        request.setFirstName("John");
        request.setLastName("Do"); 
        request.setPassword("password123");

        Set<ConstraintViolation<SignupRequest>> violations = validator.validate(request);

        assertThat(violations).isNotEmpty();
        assertThat(violations).anyMatch(v -> v.getPropertyPath().toString().equals("lastName"));
    }

    @Test
    @DisplayName("LastName with max size should be valid")
    void lastName_WithMaxSize_ShouldBeValid() {
        SignupRequest request = new SignupRequest();
        request.setEmail("test@test.com");
        request.setFirstName("John");
        request.setLastName("D".repeat(20)); 
        request.setPassword("password123");

        Set<ConstraintViolation<SignupRequest>> violations = validator.validate(request);

        assertThat(violations).isEmpty();
    }

    @Test
    @DisplayName("LastName exceeding max size should be invalid")
    void lastName_ExceedingMaxSize_ShouldBeInvalid() {
        SignupRequest request = new SignupRequest();
        request.setEmail("test@test.com");
        request.setFirstName("John");
        request.setLastName("D".repeat(21));
        request.setPassword("password123");

        Set<ConstraintViolation<SignupRequest>> violations = validator.validate(request);

        assertThat(violations).isNotEmpty();
        assertThat(violations).anyMatch(v -> v.getPropertyPath().toString().equals("lastName"));
    }

    @Test
    @DisplayName("Password cannot be null")
    void password_CannotBeNull() {
        SignupRequest request = new SignupRequest();
        request.setEmail("test@test.com");
        request.setFirstName("John");
        request.setLastName("Doe");
        request.setPassword(null);

        Set<ConstraintViolation<SignupRequest>> violations = validator.validate(request);

        assertThat(violations).isNotEmpty();
        assertThat(violations).anyMatch(v -> v.getPropertyPath().toString().equals("password"));
    }

    @Test
    @DisplayName("Password cannot be empty")
    void password_CannotBeEmpty() {
        SignupRequest request = new SignupRequest();
        request.setEmail("test@test.com");
        request.setFirstName("John");
        request.setLastName("Doe");
        request.setPassword("");

        Set<ConstraintViolation<SignupRequest>> violations = validator.validate(request);

        assertThat(violations).isNotEmpty();
        assertThat(violations).anyMatch(v -> v.getPropertyPath().toString().equals("password"));
    }

    @Test
    @DisplayName("Password with min size should be valid")
    void password_WithMinSize_ShouldBeValid() {
        SignupRequest request = new SignupRequest();
        request.setEmail("test@test.com");
        request.setFirstName("John");
        request.setLastName("Doe");
        request.setPassword("pass12");

        Set<ConstraintViolation<SignupRequest>> violations = validator.validate(request);

        assertThat(violations).isEmpty();
    }

    @Test
    @DisplayName("Password below min size should be invalid")
    void password_BelowMinSize_ShouldBeInvalid() {
        SignupRequest request = new SignupRequest();
        request.setEmail("test@test.com");
        request.setFirstName("John");
        request.setLastName("Doe");
        request.setPassword("pass1"); 

        Set<ConstraintViolation<SignupRequest>> violations = validator.validate(request);

        assertThat(violations).isNotEmpty();
        assertThat(violations).anyMatch(v -> v.getPropertyPath().toString().equals("password"));
    }

    @Test
    @DisplayName("Password with max size should be valid")
    void password_WithMaxSize_ShouldBeValid() {
        SignupRequest request = new SignupRequest();
        request.setEmail("test@test.com");
        request.setFirstName("John");
        request.setLastName("Doe");
        request.setPassword("p".repeat(40)); 

        Set<ConstraintViolation<SignupRequest>> violations = validator.validate(request);

        assertThat(violations).isEmpty();
    }

    @Test
    @DisplayName("Password exceeding max size should be invalid")
    void password_ExceedingMaxSize_ShouldBeInvalid() {
        SignupRequest request = new SignupRequest();
        request.setEmail("test@test.com");
        request.setFirstName("John");
        request.setLastName("Doe");
        request.setPassword("p".repeat(41));

        Set<ConstraintViolation<SignupRequest>> violations = validator.validate(request);

        assertThat(violations).isNotEmpty();
        assertThat(violations).anyMatch(v -> v.getPropertyPath().toString().equals("password"));
    }

    @Test
    @DisplayName("Equals should work correctly")
    void equals_ShouldWorkCorrectly() {
        SignupRequest request1 = new SignupRequest();
        request1.setEmail("test@test.com");
        request1.setFirstName("John");
        request1.setLastName("Doe");
        request1.setPassword("password123");

        SignupRequest request2 = new SignupRequest();
        request2.setEmail("test@test.com");
        request2.setFirstName("John");
        request2.setLastName("Doe");
        request2.setPassword("password123");

        assertThat(request1).isEqualTo(request2);
    }

    @Test
    @DisplayName("HashCode should work correctly")
    void hashCode_ShouldWorkCorrectly() {
        SignupRequest request1 = new SignupRequest();
        request1.setEmail("test@test.com");
        request1.setFirstName("John");
        request1.setLastName("Doe");
        request1.setPassword("password123");

        SignupRequest request2 = new SignupRequest();
        request2.setEmail("test@test.com");
        request2.setFirstName("John");
        request2.setLastName("Doe");
        request2.setPassword("password123");

        assertThat(request1.hashCode()).isEqualTo(request2.hashCode());
    }

    @Test
    @DisplayName("ToString should contain class name")
    void toString_ShouldContainClassName() {
        SignupRequest request = new SignupRequest();
        request.setEmail("test@test.com");
        request.setFirstName("John");
        request.setLastName("Doe");
        request.setPassword("password123");

        String toString = request.toString();

        assertThat(toString).contains("SignupRequest");
    }
}
