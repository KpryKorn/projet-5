package com.openclassrooms.starterjwt.models;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.NullAndEmptySource;
import org.junit.jupiter.params.provider.NullSource;
import org.junit.jupiter.params.provider.ValueSource;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;
import java.time.LocalDateTime;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("User Model Tests")
class UserTest {

    private LocalDateTime fixedDateTime;
    private Validator validator;

    @BeforeEach
    void setUp() {
        fixedDateTime = LocalDateTime.of(2025, 11, 21, 10, 0, 0);
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        validator = factory.getValidator();
    }

    @Nested
    @DisplayName("Constructor Tests")
    class ConstructorTests {

        @Test
        @DisplayName("Builder should create user with all fields")
        void builder_ShouldCreateUserWithAllFields() {
            User user = User.builder()
                    .id(1L)
                    .email("test@test.com")
                    .lastName("Doe")
                    .firstName("John")
                    .password("password123")
                    .admin(true)
                    .createdAt(fixedDateTime)
                    .updatedAt(fixedDateTime)
                    .build();

            assertThat(user.getId()).isEqualTo(1L);
            assertThat(user.getEmail()).isEqualTo("test@test.com");
            assertThat(user.getLastName()).isEqualTo("Doe");
            assertThat(user.getFirstName()).isEqualTo("John");
            assertThat(user.getPassword()).isEqualTo("password123");
            assertThat(user.isAdmin()).isTrue();
            assertThat(user.getCreatedAt()).isEqualTo(fixedDateTime);
            assertThat(user.getUpdatedAt()).isEqualTo(fixedDateTime);
        }

        @Test
        @DisplayName("No-args constructor should create empty user")
        void noArgsConstructor_ShouldCreateEmptyUser() {
            User user = new User();

            assertThat(user).isNotNull();
            assertThat(user.getId()).isNull();
            assertThat(user.getEmail()).isNull();
            assertThat(user.getLastName()).isNull();
            assertThat(user.getFirstName()).isNull();
            assertThat(user.getPassword()).isNull();
            assertThat(user.getCreatedAt()).isNull();
            assertThat(user.getUpdatedAt()).isNull();
        }

        @Test
        @DisplayName("Required args constructor should create user with required fields")
        void requiredArgsConstructor_ShouldCreateUserWithRequiredFields() {
            User user = new User("test@test.com", "Doe", "John", "password123", true);

            assertThat(user.getEmail()).isEqualTo("test@test.com");
            assertThat(user.getLastName()).isEqualTo("Doe");
            assertThat(user.getFirstName()).isEqualTo("John");
            assertThat(user.getPassword()).isEqualTo("password123");
            assertThat(user.isAdmin()).isTrue();
            assertThat(user.getId()).isNull();
            assertThat(user.getCreatedAt()).isNull();
            assertThat(user.getUpdatedAt()).isNull();
        }

        @Test
        @DisplayName("All args constructor should create user with all fields")
        void allArgsConstructor_ShouldCreateUserWithAllFields() {
            User user = new User(1L, "test@test.com", "Doe", "John", "password", false, fixedDateTime, fixedDateTime);

            assertThat(user.getId()).isEqualTo(1L);
            assertThat(user.getEmail()).isEqualTo("test@test.com");
            assertThat(user.getLastName()).isEqualTo("Doe");
            assertThat(user.getFirstName()).isEqualTo("John");
            assertThat(user.getPassword()).isEqualTo("password");
            assertThat(user.isAdmin()).isFalse();
            assertThat(user.getCreatedAt()).isEqualTo(fixedDateTime);
            assertThat(user.getUpdatedAt()).isEqualTo(fixedDateTime);
        }

        @Test
        @DisplayName("Required args constructor with null email should throw NullPointerException")
        void requiredArgsConstructor_WithNullEmail_ShouldThrowException() {
            try {
                new User(null, "Doe", "John", "password", false);
                // Si on arrive ici, le test échoue
                assertThat(false).isTrue();
            } catch (NullPointerException e) {
                assertThat(e.getMessage()).contains("email");
            }
        }

        @Test
        @DisplayName("Required args constructor with null lastName should throw NullPointerException")
        void requiredArgsConstructor_WithNullLastName_ShouldThrowException() {
            try {
                new User("test@test.com", null, "John", "password", false);
                assertThat(false).isTrue();
            } catch (NullPointerException e) {
                assertThat(e.getMessage()).contains("lastName");
            }
        }

        @Test
        @DisplayName("Required args constructor with null firstName should throw NullPointerException")
        void requiredArgsConstructor_WithNullFirstName_ShouldThrowException() {
            try {
                new User("test@test.com", "Doe", null, "password", false);
                assertThat(false).isTrue();
            } catch (NullPointerException e) {
                assertThat(e.getMessage()).contains("firstName");
            }
        }

        @Test
        @DisplayName("Required args constructor with null password should throw NullPointerException")
        void requiredArgsConstructor_WithNullPassword_ShouldThrowException() {
            try {
                new User("test@test.com", "Doe", "John", null, false);
                assertThat(false).isTrue();
            } catch (NullPointerException e) {
                assertThat(e.getMessage()).contains("password");
            }
        }

        @Test
        @DisplayName("All args constructor with null email should throw NullPointerException")
        void allArgsConstructor_WithNullEmail_ShouldThrowException() {
            try {
                new User(1L, null, "Doe", "John", "password", false, fixedDateTime, fixedDateTime);
                assertThat(false).isTrue();
            } catch (NullPointerException e) {
                assertThat(e.getMessage()).contains("email");
            }
        }

        @Test
        @DisplayName("All args constructor with null lastName should throw NullPointerException")
        void allArgsConstructor_WithNullLastName_ShouldThrowException() {
            try {
                new User(1L, "test@test.com", null, "John", "password", false, fixedDateTime, fixedDateTime);
                assertThat(false).isTrue();
            } catch (NullPointerException e) {
                assertThat(e.getMessage()).contains("lastName");
            }
        }

        @Test
        @DisplayName("All args constructor with null firstName should throw NullPointerException")
        void allArgsConstructor_WithNullFirstName_ShouldThrowException() {
            try {
                new User(1L, "test@test.com", "Doe", null, "password", false, fixedDateTime, fixedDateTime);
                assertThat(false).isTrue();
            } catch (NullPointerException e) {
                assertThat(e.getMessage()).contains("firstName");
            }
        }

        @Test
        @DisplayName("All args constructor with null password should throw NullPointerException")
        void allArgsConstructor_WithNullPassword_ShouldThrowException() {
            try {
                new User(1L, "test@test.com", "Doe", "John", null, false, fixedDateTime, fixedDateTime);
                assertThat(false).isTrue();
            } catch (NullPointerException e) {
                assertThat(e.getMessage()).contains("password");
            }
        }

        @Test
        @DisplayName("All args constructor with null timestamps should work")
        void allArgsConstructor_WithNullTimestamps_ShouldWork() {
            User user = new User(1L, "test@test.com", "Doe", "John", "password", false, null, null);

            assertThat(user.getId()).isEqualTo(1L);
            assertThat(user.getEmail()).isEqualTo("test@test.com");
            assertThat(user.getCreatedAt()).isNull();
            assertThat(user.getUpdatedAt()).isNull();
        }

        @Test
        @DisplayName("All args constructor with null id should work")
        void allArgsConstructor_WithNullId_ShouldWork() {
            User user = new User(null, "test@test.com", "Doe", "John", "password", false, fixedDateTime, fixedDateTime);

            assertThat(user.getId()).isNull();
            assertThat(user.getEmail()).isEqualTo("test@test.com");
        }
    }

    @Nested
    @DisplayName("UserBuilder Tests")
    class UserBuilderTests {

        @Test
        @DisplayName("Builder should allow setting each field individually")
        void builder_ShouldAllowSettingEachFieldIndividually() {
            User.UserBuilder builder = User.builder();
            
            builder.id(99L);
            builder.email("builder@test.com");
            builder.lastName("BuilderLast");
            builder.firstName("BuilderFirst");
            builder.password("builderPass");
            builder.admin(true);
            builder.createdAt(fixedDateTime);
            builder.updatedAt(fixedDateTime);
            
            User user = builder.build();

            assertThat(user.getId()).isEqualTo(99L);
            assertThat(user.getEmail()).isEqualTo("builder@test.com");
            assertThat(user.getLastName()).isEqualTo("BuilderLast");
            assertThat(user.getFirstName()).isEqualTo("BuilderFirst");
            assertThat(user.getPassword()).isEqualTo("builderPass");
            assertThat(user.isAdmin()).isTrue();
            assertThat(user.getCreatedAt()).isEqualTo(fixedDateTime);
            assertThat(user.getUpdatedAt()).isEqualTo(fixedDateTime);
        }

        @Test
        @DisplayName("Builder should allow chaining method calls")
        void builder_ShouldAllowChainingMethodCalls() {
            User user = User.builder()
                    .id(1L)
                    .email("chain@test.com")
                    .lastName("Chain")
                    .firstName("Method")
                    .password("chainPass")
                    .admin(false)
                    .build();

            assertThat(user).isNotNull();
            assertThat(user.getId()).isEqualTo(1L);
            assertThat(user.getEmail()).isEqualTo("chain@test.com");
        }

        @Test
        @DisplayName("Builder should create user with only required fields")
        void builder_ShouldCreateUserWithOnlyRequiredFields() {
            User user = User.builder()
                    .email("minimal@test.com")
                    .lastName("Minimal")
                    .firstName("User")
                    .password("minimalPass")
                    .admin(false)
                    .build();

            assertThat(user.getId()).isNull();
            assertThat(user.getEmail()).isEqualTo("minimal@test.com");
            assertThat(user.getLastName()).isEqualTo("Minimal");
            assertThat(user.getFirstName()).isEqualTo("User");
            assertThat(user.getPassword()).isEqualTo("minimalPass");
            assertThat(user.isAdmin()).isFalse();
            assertThat(user.getCreatedAt()).isNull();
            assertThat(user.getUpdatedAt()).isNull();
        }

        @Test
        @DisplayName("Builder should handle null optional values")
        void builder_ShouldHandleNullOptionalValues() {
            User user = User.builder()
                    .id(null)
                    .email("test@test.com")
                    .lastName("Doe")
                    .firstName("John")
                    .password("pass")
                    .admin(false)
                    .createdAt(null)
                    .updatedAt(null)
                    .build();

            assertThat(user).isNotNull();
            assertThat(user.getId()).isNull();
            assertThat(user.getCreatedAt()).isNull();
            assertThat(user.getUpdatedAt()).isNull();
        }

        @Test
        @DisplayName("Builder should create independent user instances")
        void builder_ShouldCreateIndependentUserInstances() {
            User.UserBuilder builder = User.builder();
            
            User user1 = builder.email("user1@test.com").lastName("User1").firstName("First1").password("pass1").admin(false).build();
            User user2 = User.builder().email("user2@test.com").lastName("User2").firstName("First2").password("pass2").admin(true).build();

            assertThat(user1.getEmail()).isNotEqualTo(user2.getEmail());
            assertThat(user1.getLastName()).isNotEqualTo(user2.getLastName());
            assertThat(user1.isAdmin()).isNotEqualTo(user2.isAdmin());
        }

        @Test
        @DisplayName("Builder toString should contain UserBuilder")
        void builder_ToStringShouldContainUserBuilder() {
            User.UserBuilder builder = User.builder().email("test@test.com");
            
            String builderString = builder.toString();
            
            assertThat(builderString).contains("UserBuilder");
        }
    }

    @Nested
    @DisplayName("Setters and Getters Tests")
    class SettersAndGettersTests {

        @Test
        @DisplayName("Setters should update all fields correctly")
        void setters_ShouldUpdateAllFields() {
            User user = new User();
            LocalDateTime now = LocalDateTime.now();

            user.setId(2L);
            user.setEmail("user@test.com");
            user.setLastName("Smith");
            user.setFirstName("Jane");
            user.setPassword("pass456");
            user.setAdmin(false);
            user.setCreatedAt(now);
            user.setUpdatedAt(now);

            assertThat(user.getId()).isEqualTo(2L);
            assertThat(user.getEmail()).isEqualTo("user@test.com");
            assertThat(user.getLastName()).isEqualTo("Smith");
            assertThat(user.getFirstName()).isEqualTo("Jane");
            assertThat(user.getPassword()).isEqualTo("pass456");
            assertThat(user.isAdmin()).isFalse();
            assertThat(user.getCreatedAt()).isEqualTo(now);
            assertThat(user.getUpdatedAt()).isEqualTo(now);
        }

        @Test
        @DisplayName("Chained setters should return this instance")
        void chainedSetters_ShouldReturnThisInstance() {
            User user = new User()
                    .setId(3L)
                    .setEmail("chain@test.com")
                    .setLastName("Chain")
                    .setFirstName("Test")
                    .setPassword("chainpass")
                    .setAdmin(true);

            assertThat(user.getId()).isEqualTo(3L);
            assertThat(user.getEmail()).isEqualTo("chain@test.com");
            assertThat(user.getLastName()).isEqualTo("Chain");
            assertThat(user.getFirstName()).isEqualTo("Test");
            assertThat(user.getPassword()).isEqualTo("chainpass");
            assertThat(user.isAdmin()).isTrue();
        }

        @Test
        @DisplayName("Getters should return correct values")
        void getters_ShouldReturnCorrectValues() {
            LocalDateTime created = LocalDateTime.now().minusDays(1);
            LocalDateTime updated = LocalDateTime.now();
            
            User user = User.builder()
                    .id(99L)
                    .email("complete@test.com")
                    .lastName("Complete")
                    .firstName("User")
                    .password("completePass")
                    .admin(true)
                    .createdAt(created)
                    .updatedAt(updated)
                    .build();

            assertThat(user.getId()).isEqualTo(99L);
            assertThat(user.getEmail()).isEqualTo("complete@test.com");
            assertThat(user.getLastName()).isEqualTo("Complete");
            assertThat(user.getFirstName()).isEqualTo("User");
            assertThat(user.getPassword()).isEqualTo("completePass");
            assertThat(user.isAdmin()).isTrue();
            assertThat(user.getCreatedAt()).isEqualTo(created);
            assertThat(user.getUpdatedAt()).isEqualTo(updated);
        }

        @Test
        @DisplayName("isAdmin should return correct boolean value for admin user")
        void isAdmin_ShouldReturnTrueForAdmin() {
            User adminUser = User.builder().email("admin@test.com").lastName("Admin").firstName("User").password("pass").admin(true).build();
            assertThat(adminUser.isAdmin()).isTrue();
        }

        @Test
        @DisplayName("isAdmin should return correct boolean value for non-admin user")
        void isAdmin_ShouldReturnFalseForNonAdmin() {
            User regularUser = User.builder().email("user@test.com").lastName("Regular").firstName("User").password("pass").admin(false).build();
            assertThat(regularUser.isAdmin()).isFalse();
        }
    }

    @Nested
    @DisplayName("Equals and HashCode Tests")
    class EqualsAndHashCodeTests {

        @Test
        @DisplayName("Equals should be reflexive - user equals itself")
        void equals_ShouldBeReflexive() {
            User user = User.builder()
                    .id(1L)
                    .email("test@test.com")
                    .lastName("Doe")
                    .firstName("John")
                    .password("pass")
                    .admin(false)
                    .build();

            assertThat(user.equals(user)).isTrue();
        }

        @Test
        @DisplayName("Equals should be symmetric")
        void equals_ShouldBeSymmetric() {
            User user1 = User.builder().id(1L).email("user1@test.com").lastName("Doe").firstName("John").password("pass1").admin(false).build();
            User user2 = User.builder().id(1L).email("user2@test.com").lastName("Smith").firstName("Jane").password("pass2").admin(true).build();

            assertThat(user1.equals(user2)).isTrue();
            assertThat(user2.equals(user1)).isTrue();
        }

        @Test
        @DisplayName("Equals should be transitive")
        void equals_ShouldBeTransitive() {
            User user1 = User.builder().id(1L).email("a@test.com").lastName("A").firstName("A").password("a").admin(false).build();
            User user2 = User.builder().id(1L).email("b@test.com").lastName("B").firstName("B").password("b").admin(true).build();
            User user3 = User.builder().id(1L).email("c@test.com").lastName("C").firstName("C").password("c").admin(false).build();

            assertThat(user1.equals(user2)).isTrue();
            assertThat(user2.equals(user3)).isTrue();
            assertThat(user1.equals(user3)).isTrue();
        }

        @Test
        @DisplayName("Equals with null should return false")
        void equals_WithNull_ShouldReturnFalse() {
            User user = User.builder().id(1L).email("test@test.com").lastName("Doe").firstName("John").password("pass").admin(false).build();
            assertThat(user.equals(null)).isFalse();
        }

        @Test
        @DisplayName("Equals with different type should return false")
        void equals_WithDifferentType_ShouldReturnFalse() {
            User user = User.builder().id(1L).email("test@test.com").lastName("Doe").firstName("John").password("pass").admin(false).build();
            String differentType = "Not a User";
            assertThat(user.equals(differentType)).isFalse();
        }

        @Test
        @DisplayName("Equals with same id should return true")
        void equals_WithSameId_ShouldReturnTrue() {
            User user1 = User.builder().id(1L).email("user1@test.com").lastName("Doe").firstName("John").password("pass1").admin(false).build();
            User user2 = User.builder().id(1L).email("user2@test.com").lastName("Smith").firstName("Jane").password("pass2").admin(true).build();

            assertThat(user1.equals(user2)).isTrue();
        }

        @Test
        @DisplayName("Equals with different id should return false")
        void equals_WithDifferentId_ShouldReturnFalse() {
            User user1 = User.builder().id(1L).email("test@test.com").lastName("Doe").firstName("John").password("pass").admin(false).build();
            User user2 = User.builder().id(2L).email("test@test.com").lastName("Doe").firstName("John").password("pass").admin(false).build();

            assertThat(user1.equals(user2)).isFalse();
        }

        @Test
        @DisplayName("HashCode should be consistent with equals for same id")
        void hashCode_ShouldBeConsistentWithEquals() {
            User user1 = User.builder().id(1L).email("test1@test.com").lastName("Doe").firstName("John").password("pass").admin(false).build();
            User user2 = User.builder().id(1L).email("test2@test.com").lastName("Smith").firstName("Jane").password("pass2").admin(true).build();

            assertThat(user1.hashCode()).isEqualTo(user2.hashCode());
        }

        @Test
        @DisplayName("HashCode should be stable across multiple invocations")
        void hashCode_ShouldBeStable() {
            User user = User.builder().id(1L).email("test@test.com").lastName("Doe").firstName("John").password("pass").admin(false).build();
            int hashCode1 = user.hashCode();
            int hashCode2 = user.hashCode();

            assertThat(hashCode1).isEqualTo(hashCode2);
        }

        @Test
        @DisplayName("HashCode should differ for different ids")
        void hashCode_ShouldDifferForDifferentIds() {
            User user1 = User.builder().id(1L).email("test@test.com").lastName("Doe").firstName("John").password("pass").admin(false).build();
            User user2 = User.builder().id(2L).email("test@test.com").lastName("Doe").firstName("John").password("pass").admin(false).build();

            assertThat(user1.hashCode()).isNotEqualTo(user2.hashCode());
        }

        @Test
        @DisplayName("Two users with null IDs should be handled correctly")
        void equals_WithBothNullIds_ShouldWorkCorrectly() {
            User user1 = User.builder().email("test1@test.com").lastName("Doe").firstName("John").password("pass").admin(false).build();
            User user2 = User.builder().email("test2@test.com").lastName("Smith").firstName("Jane").password("pass").admin(false).build();

            boolean areEqual = user1.equals(user2);
            assertThat(areEqual).isEqualTo(user1.getId() == user2.getId());
        }
    }

    @Nested
    @DisplayName("ToString Tests")
    class ToStringTests {

        @Test
        @DisplayName("ToString should contain class name")
        void toString_ShouldContainClassName() {
            User user = User.builder()
                    .id(1L)
                    .email("test@test.com")
                    .lastName("Doe")
                    .firstName("John")
                    .password("password")
                    .admin(false)
                    .build();

            String toString = user.toString();
            assertThat(toString).contains("User");
        }

        @Test
        @DisplayName("ToString should be consistent")
        void toString_ShouldBeConsistent() {
            User user = User.builder()
                    .id(1L)
                    .email("test@test.com")
                    .lastName("Doe")
                    .firstName("John")
                    .password("pass")
                    .admin(false)
                    .build();

            String toString1 = user.toString();
            String toString2 = user.toString();
            assertThat(toString1).isEqualTo(toString2);
        }
    }

    @Nested
    @DisplayName("Validation Tests")
    class ValidationTests {

        @Test
        @DisplayName("Valid user should pass validation")
        void validUser_ShouldPassValidation() {
            User user = User.builder()
                    .email("valid@test.com")
                    .lastName("Valid")
                    .firstName("User")
                    .password("validPass")
                    .admin(false)
                    .build();

            Set<ConstraintViolation<User>> violations = validator.validate(user);
            assertThat(violations).isEmpty();
        }

        @Test
        @DisplayName("User with max size fields should be valid")
        void user_WithMaxSizeFields_ShouldBeValid() {
            String maxEmail = "a".repeat(40) + "@test.com";
            String maxLastName = "L".repeat(20); 
            String maxFirstName = "F".repeat(20);
            String maxPassword = "P".repeat(120);

            User user = User.builder()
                    .email(maxEmail)
                    .lastName(maxLastName)
                    .firstName(maxFirstName)
                    .password(maxPassword)
                    .admin(false)
                    .build();

            Set<ConstraintViolation<User>> violations = validator.validate(user);
            assertThat(violations).isEmpty();
        }

        @Test
        @DisplayName("User with email exceeding max size should be invalid")
        void user_WithEmailExceedingMaxSize_ShouldBeInvalid() {
            String tooLongEmail = "a".repeat(50) + "@test.com";
            User user = User.builder()
                    .email(tooLongEmail)
                    .lastName("Doe")
                    .firstName("John")
                    .password("pass")
                    .admin(false)
                    .build();

            Set<ConstraintViolation<User>> violations = validator.validate(user);
            assertThat(violations).isNotEmpty();
        }

        @Test
        @DisplayName("User with invalid email format should be invalid")
        void user_WithInvalidEmailFormat_ShouldBeInvalid() {
            User user = User.builder()
                    .email("not-an-email")
                    .lastName("Doe")
                    .firstName("John")
                    .password("pass")
                    .admin(false)
                    .build();

            Set<ConstraintViolation<User>> violations = validator.validate(user);
            assertThat(violations).isNotEmpty();
        }
    }

    @Nested
    @DisplayName("Edge Cases Tests")
    class EdgeCasesTests {

        @Test
        @DisplayName("User with minimum valid data should work")
        void user_WithMinimumValidData_ShouldWork() {
            User user = User.builder()
                    .email("a@b.c")
                    .lastName("D")
                    .firstName("J")
                    .password("p")
                    .admin(false)
                    .build();

            assertThat(user.getEmail()).isEqualTo("a@b.c");
            assertThat(user.getLastName()).isEqualTo("D");
            assertThat(user.getFirstName()).isEqualTo("J");
            assertThat(user.getPassword()).isEqualTo("p");
            assertThat(user.isAdmin()).isFalse();
        }

        @Test
        @DisplayName("User with special characters in names should work")
        void user_WithSpecialCharactersInNames_ShouldWork() {
            User user = User.builder()
                    .email("test@test.com")
                    .lastName("O'Brien")
                    .firstName("Jean-Paul")
                    .password("p@ssw0rd!")
                    .admin(false)
                    .build();

            assertThat(user.getLastName()).isEqualTo("O'Brien");
            assertThat(user.getFirstName()).isEqualTo("Jean-Paul");
            assertThat(user.getPassword()).isEqualTo("p@ssw0rd!");
        }

        @Test
        @DisplayName("User timestamps should be independent")
        void user_TimestampsShouldBeIndependent() {
            LocalDateTime created = LocalDateTime.now().minusDays(1);
            LocalDateTime updated = LocalDateTime.now();
            
            User user = User.builder()
                    .email("test@test.com")
                    .lastName("Doe")
                    .firstName("John")
                    .password("pass")
                    .admin(false)
                    .createdAt(created)
                    .updatedAt(updated)
                    .build();

            assertThat(user.getCreatedAt()).isNotEqualTo(user.getUpdatedAt());
            assertThat(user.getUpdatedAt()).isAfter(user.getCreatedAt());
        }

        @Test
        @DisplayName("User with null id should work")
        void user_WithNullId_ShouldWork() {
            User user = User.builder()
                    .id(null)
                    .email("test@test.com")
                    .lastName("Doe")
                    .firstName("John")
                    .password("pass")
                    .admin(false)
                    .build();

            assertThat(user.getId()).isNull();
        }

        @Test
        @DisplayName("Admin flag should toggle correctly")
        void adminFlag_ShouldToggleCorrectly() {
            User user = new User();
            user.setEmail("test@test.com");
            user.setLastName("Doe");
            user.setFirstName("John");
            user.setPassword("pass");
            
            user.setAdmin(true);
            assertThat(user.isAdmin()).isTrue();
            
            user.setAdmin(false);
            assertThat(user.isAdmin()).isFalse();
        }
    }
}
