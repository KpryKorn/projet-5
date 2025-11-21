package com.openclassrooms.starterjwt.models;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.NullSource;
import org.junit.jupiter.params.provider.ValueSource;

import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("Teacher Model Tests")
class TeacherTest {

    private LocalDateTime fixedDateTime;

    @BeforeEach
    void setUp() {
        fixedDateTime = LocalDateTime.of(2025, 11, 21, 10, 0, 0);
    }

    @Nested
    @DisplayName("Constructor Tests")
    class ConstructorTests {

        @Test
        @DisplayName("Builder should create teacher with all fields")
        void builder_ShouldCreateTeacherWithAllFields() {
            Teacher teacher = Teacher.builder()
                    .id(1L)
                    .lastName("Smith")
                    .firstName("Jane")
                    .createdAt(fixedDateTime)
                    .updatedAt(fixedDateTime)
                    .build();

            assertThat(teacher.getId()).isEqualTo(1L);
            assertThat(teacher.getLastName()).isEqualTo("Smith");
            assertThat(teacher.getFirstName()).isEqualTo("Jane");
            assertThat(teacher.getCreatedAt()).isEqualTo(fixedDateTime);
            assertThat(teacher.getUpdatedAt()).isEqualTo(fixedDateTime);
        }

        @Test
        @DisplayName("No-args constructor should create empty teacher")
        void noArgsConstructor_ShouldCreateEmptyTeacher() {
            Teacher teacher = new Teacher();

            assertThat(teacher).isNotNull();
            assertThat(teacher.getId()).isNull();
            assertThat(teacher.getLastName()).isNull();
            assertThat(teacher.getFirstName()).isNull();
            assertThat(teacher.getCreatedAt()).isNull();
            assertThat(teacher.getUpdatedAt()).isNull();
        }

        @Test
        @DisplayName("All args constructor should create teacher with all fields")
        void allArgsConstructor_ShouldCreateTeacherWithAllFields() {
            Teacher teacher = new Teacher(1L, "Martinez", "Carlos", fixedDateTime, fixedDateTime);

            assertThat(teacher.getId()).isEqualTo(1L);
            assertThat(teacher.getLastName()).isEqualTo("Martinez");
            assertThat(teacher.getFirstName()).isEqualTo("Carlos");
            assertThat(teacher.getCreatedAt()).isEqualTo(fixedDateTime);
            assertThat(teacher.getUpdatedAt()).isEqualTo(fixedDateTime);
        }

        @Test
        @DisplayName("Builder with minimal fields should create teacher")
        void builder_WithMinimalFields_ShouldCreateTeacher() {
            Teacher teacher = Teacher.builder()
                    .lastName("Minimal")
                    .firstName("Test")
                    .build();

            assertThat(teacher.getLastName()).isEqualTo("Minimal");
            assertThat(teacher.getFirstName()).isEqualTo("Test");
            assertThat(teacher.getId()).isNull();
            assertThat(teacher.getCreatedAt()).isNull();
            assertThat(teacher.getUpdatedAt()).isNull();
        }

        @Test
        @DisplayName("TeacherBuilder toString should contain TeacherBuilder")
        void teacherBuilder_ToString_ShouldContainTeacherBuilder() {
            Teacher.TeacherBuilder builder = Teacher.builder()
                    .lastName("TestLastName")
                    .firstName("TestFirstName");

            String builderString = builder.toString();

            assertThat(builderString).contains("TeacherBuilder");
        }

        @Test
        @DisplayName("TeacherBuilder should allow setting each field individually")
        void teacherBuilder_ShouldAllowSettingEachFieldIndividually() {
            Teacher.TeacherBuilder builder = Teacher.builder();

            builder.id(99L);
            builder.lastName("BuilderLast");
            builder.firstName("BuilderFirst");
            builder.createdAt(fixedDateTime);
            builder.updatedAt(fixedDateTime);

            Teacher teacher = builder.build();

            assertThat(teacher.getId()).isEqualTo(99L);
            assertThat(teacher.getLastName()).isEqualTo("BuilderLast");
            assertThat(teacher.getFirstName()).isEqualTo("BuilderFirst");
            assertThat(teacher.getCreatedAt()).isEqualTo(fixedDateTime);
            assertThat(teacher.getUpdatedAt()).isEqualTo(fixedDateTime);
        }

        @Test
        @DisplayName("TeacherBuilder should allow chaining method calls")
        void teacherBuilder_ShouldAllowChainingMethodCalls() {
            Teacher teacher = Teacher.builder()
                    .id(1L)
                    .lastName("Chained")
                    .firstName("Method")
                    .build();

            assertThat(teacher).isNotNull();
            assertThat(teacher.getId()).isEqualTo(1L);
            assertThat(teacher.getLastName()).isEqualTo("Chained");
            assertThat(teacher.getFirstName()).isEqualTo("Method");
        }

        @Test
        @DisplayName("TeacherBuilder should create independent teacher instances")
        void teacherBuilder_ShouldCreateIndependentTeacherInstances() {
            Teacher.TeacherBuilder builder = Teacher.builder();

            Teacher teacher1 = builder.lastName("Teacher1").firstName("First1").build();
            Teacher teacher2 = Teacher.builder().lastName("Teacher2").firstName("First2").build();

            assertThat(teacher1.getLastName()).isNotEqualTo(teacher2.getLastName());
            assertThat(teacher1.getFirstName()).isNotEqualTo(teacher2.getFirstName());
        }
    }

    @Test
    void setters_ShouldUpdateFields() {
        Teacher teacher = new Teacher();
        LocalDateTime now = LocalDateTime.now();

        teacher.setId(2L);
        teacher.setLastName("Johnson");
        teacher.setFirstName("Bob");
        teacher.setCreatedAt(now);
        teacher.setUpdatedAt(now);

        assertThat(teacher.getId()).isEqualTo(2L);
        assertThat(teacher.getLastName()).isEqualTo("Johnson");
        assertThat(teacher.getFirstName()).isEqualTo("Bob");
        assertThat(teacher.getCreatedAt()).isEqualTo(now);
        assertThat(teacher.getUpdatedAt()).isEqualTo(now);
    }

    @Test
    void chainedSetters_ShouldReturnThisInstance() {
        Teacher teacher = new Teacher()
                .setId(3L)
                .setLastName("Brown")
                .setFirstName("Alice");

        assertThat(teacher.getId()).isEqualTo(3L);
        assertThat(teacher.getLastName()).isEqualTo("Brown");
        assertThat(teacher.getFirstName()).isEqualTo("Alice");
    }

    @Test
    void equals_WithSameId_ShouldReturnTrue() {
        Teacher teacher1 = Teacher.builder()
                .id(1L)
                .lastName("Smith")
                .firstName("Jane")
                .build();

        Teacher teacher2 = Teacher.builder()
                .id(1L)
                .lastName("Doe")
                .firstName("John")
                .build();

        assertThat(teacher1.equals(teacher2)).isTrue();
    }

    @Test
    void equals_WithDifferentId_ShouldReturnFalse() {
        Teacher teacher1 = Teacher.builder()
                .id(1L)
                .lastName("Smith")
                .firstName("Jane")
                .build();

        Teacher teacher2 = Teacher.builder()
                .id(2L)
                .lastName("Smith")
                .firstName("Jane")
                .build();

        assertThat(teacher1.equals(teacher2)).isFalse();
    }

    @Test
    void hashCode_WithSameId_ShouldBeEqual() {
        Teacher teacher1 = Teacher.builder().id(1L).lastName("Smith").firstName("Jane").build();
        Teacher teacher2 = Teacher.builder().id(1L).lastName("Doe").firstName("John").build();

        assertThat(teacher1.hashCode()).isEqualTo(teacher2.hashCode());
    }

    @Test
    void toString_ShouldContainClassName() {
        Teacher teacher = Teacher.builder()
                .id(1L)
                .lastName("Smith")
                .firstName("Jane")
                .build();

        String toString = teacher.toString();

        assertThat(toString).contains("Teacher");
    }

    @Test
    void allArgsConstructor_ShouldCreateTeacherWithAllFields() {
        LocalDateTime now = LocalDateTime.now();
        
        Teacher teacher = new Teacher(1L, "Martinez", "Carlos", now, now);

        assertThat(teacher.getId()).isEqualTo(1L);
        assertThat(teacher.getLastName()).isEqualTo("Martinez");
        assertThat(teacher.getFirstName()).isEqualTo("Carlos");
        assertThat(teacher.getCreatedAt()).isEqualTo(now);
        assertThat(teacher.getUpdatedAt()).isEqualTo(now);
    }

    @Test
    void getters_ShouldReturnCorrectValues() {
        LocalDateTime created = LocalDateTime.now().minusDays(1);
        LocalDateTime updated = LocalDateTime.now();
        
        Teacher teacher = Teacher.builder()
                .id(10L)
                .lastName("Wilson")
                .firstName("Emma")
                .createdAt(created)
                .updatedAt(updated)
                .build();

        assertThat(teacher.getId()).isEqualTo(10L);
        assertThat(teacher.getLastName()).isEqualTo("Wilson");
        assertThat(teacher.getFirstName()).isEqualTo("Emma");
        assertThat(teacher.getCreatedAt()).isEqualTo(created);
        assertThat(teacher.getUpdatedAt()).isEqualTo(updated);
    }

    @Test
    void builder_WithMinimalFields_ShouldCreateTeacher() {
        Teacher teacher = Teacher.builder()
                .lastName("Minimal")
                .firstName("Test")
                .build();

        assertThat(teacher.getLastName()).isEqualTo("Minimal");
        assertThat(teacher.getFirstName()).isEqualTo("Test");
        assertThat(teacher.getId()).isNull();
        assertThat(teacher.getCreatedAt()).isNull();
        assertThat(teacher.getUpdatedAt()).isNull();
    }
}
