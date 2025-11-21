package com.openclassrooms.starterjwt.models;

import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

class SessionTest {

    @Test
    void builder_ShouldCreateSessionWithAllFields() {
        LocalDateTime now = LocalDateTime.now();
        Date date = new Date();
        Teacher teacher = Teacher.builder().id(1L).lastName("Smith").firstName("Jane").build();
        User user1 = User.builder().id(1L).email("user1@test.com").lastName("Doe").firstName("John").password("pass").admin(false).build();
        User user2 = User.builder().id(2L).email("user2@test.com").lastName("Smith").firstName("Jane").password("pass").admin(false).build();
        List<User> users = Arrays.asList(user1, user2);
        
        Session session = Session.builder()
                .id(1L)
                .name("Morning Yoga")
                .date(date)
                .description("Morning session description")
                .teacher(teacher)
                .users(users)
                .createdAt(now)
                .updatedAt(now)
                .build();

        assertThat(session.getId()).isEqualTo(1L);
        assertThat(session.getName()).isEqualTo("Morning Yoga");
        assertThat(session.getDate()).isEqualTo(date);
        assertThat(session.getDescription()).isEqualTo("Morning session description");
        assertThat(session.getTeacher()).isEqualTo(teacher);
        assertThat(session.getUsers()).hasSize(2);
        assertThat(session.getCreatedAt()).isEqualTo(now);
        assertThat(session.getUpdatedAt()).isEqualTo(now);
    }

    @Test
    void sessionBuilder_ToString_ShouldContainSessionBuilder() {
        Session.SessionBuilder builder = Session.builder()
                .name("Test Session")
                .date(new Date())
                .description("Test description");

        String builderString = builder.toString();

        assertThat(builderString).contains("SessionBuilder");
    }

    @Test
    void sessionBuilder_ShouldAllowSettingEachFieldIndividually() {
        Session.SessionBuilder builder = Session.builder();
        LocalDateTime now = LocalDateTime.now();
        Date date = new Date();
        Teacher teacher = Teacher.builder().id(1L).lastName("Teacher").firstName("Test").build();
        List<User> users = new ArrayList<>();

        builder.id(99L);
        builder.name("Builder Session");
        builder.date(date);
        builder.description("Builder description");
        builder.teacher(teacher);
        builder.users(users);
        builder.createdAt(now);
        builder.updatedAt(now);

        Session session = builder.build();

        assertThat(session.getId()).isEqualTo(99L);
        assertThat(session.getName()).isEqualTo("Builder Session");
        assertThat(session.getDate()).isEqualTo(date);
        assertThat(session.getDescription()).isEqualTo("Builder description");
        assertThat(session.getTeacher()).isEqualTo(teacher);
        assertThat(session.getUsers()).isEmpty();
        assertThat(session.getCreatedAt()).isEqualTo(now);
        assertThat(session.getUpdatedAt()).isEqualTo(now);
    }

    @Test
    void sessionBuilder_ShouldAllowChainingMethodCalls() {
        Date date = new Date();
        Session session = Session.builder()
                .id(1L)
                .name("Chained Session")
                .date(date)
                .description("Chained description")
                .build();

        assertThat(session).isNotNull();
        assertThat(session.getId()).isEqualTo(1L);
        assertThat(session.getName()).isEqualTo("Chained Session");
        assertThat(session.getDate()).isEqualTo(date);
    }

    @Test
    void noArgsConstructor_ShouldCreateEmptySession() {
        Session session = new Session();

        assertThat(session).isNotNull();
        assertThat(session.getId()).isNull();
        assertThat(session.getName()).isNull();
    }

    @Test
    void setters_ShouldUpdateFields() {
        Session session = new Session();
        LocalDateTime now = LocalDateTime.now();
        Date date = new Date();
        Teacher teacher = Teacher.builder().id(1L).build();
        List<User> users = new ArrayList<>();

        session.setId(2L);
        session.setName("Evening Yoga");
        session.setDate(date);
        session.setDescription("Evening session");
        session.setTeacher(teacher);
        session.setUsers(users);
        session.setCreatedAt(now);
        session.setUpdatedAt(now);

        assertThat(session.getId()).isEqualTo(2L);
        assertThat(session.getName()).isEqualTo("Evening Yoga");
        assertThat(session.getDate()).isEqualTo(date);
        assertThat(session.getDescription()).isEqualTo("Evening session");
        assertThat(session.getTeacher()).isEqualTo(teacher);
        assertThat(session.getUsers()).isEmpty();
        assertThat(session.getCreatedAt()).isEqualTo(now);
        assertThat(session.getUpdatedAt()).isEqualTo(now);
    }

    @Test
    void chainedSetters_ShouldReturnThisInstance() {
        Date date = new Date();
        Teacher teacher = Teacher.builder().id(1L).build();
        
        Session session = new Session()
                .setId(3L)
                .setName("Afternoon Session")
                .setDate(date)
                .setDescription("Afternoon yoga")
                .setTeacher(teacher);

        assertThat(session.getId()).isEqualTo(3L);
        assertThat(session.getName()).isEqualTo("Afternoon Session");
        assertThat(session.getDate()).isEqualTo(date);
        assertThat(session.getDescription()).isEqualTo("Afternoon yoga");
        assertThat(session.getTeacher()).isEqualTo(teacher);
    }

    @Test
    void equals_WithSameId_ShouldReturnTrue() {
        Session session1 = Session.builder()
                .id(1L)
                .name("Session 1")
                .date(new Date())
                .description("Description 1")
                .build();

        Session session2 = Session.builder()
                .id(1L)
                .name("Session 2")
                .date(new Date())
                .description("Description 2")
                .build();

        assertThat(session1.equals(session2)).isTrue();
    }

    @Test
    void equals_WithDifferentId_ShouldReturnFalse() {
        Session session1 = Session.builder()
                .id(1L)
                .name("Session")
                .date(new Date())
                .description("Description")
                .build();

        Session session2 = Session.builder()
                .id(2L)
                .name("Session")
                .date(new Date())
                .description("Description")
                .build();

        assertThat(session1.equals(session2)).isFalse();
    }

    @Test
    void hashCode_WithSameId_ShouldBeEqual() {
        Session session1 = Session.builder().id(1L).name("Session 1").date(new Date()).description("Desc 1").build();
        Session session2 = Session.builder().id(1L).name("Session 2").date(new Date()).description("Desc 2").build();

        assertThat(session1.hashCode()).isEqualTo(session2.hashCode());
    }

    @Test
    void toString_ShouldContainClassName() {
        Session session = Session.builder()
                .id(1L)
                .name("Test Session")
                .date(new Date())
                .description("Test description")
                .build();

        String toString = session.toString();

        assertThat(toString).contains("Session");
    }

    @Test
    void allArgsConstructor_ShouldCreateSessionWithAllFields() {
        LocalDateTime now = LocalDateTime.now();
        Date date = new Date();
        Teacher teacher = Teacher.builder().id(1L).build();
        List<User> users = new ArrayList<>();
        
        Session session = new Session(1L, "Full Session", date, "Full description", teacher, users, now, now);

        assertThat(session.getId()).isEqualTo(1L);
        assertThat(session.getName()).isEqualTo("Full Session");
        assertThat(session.getDate()).isEqualTo(date);
        assertThat(session.getDescription()).isEqualTo("Full description");
        assertThat(session.getTeacher()).isEqualTo(teacher);
        assertThat(session.getUsers()).isEmpty();
        assertThat(session.getCreatedAt()).isEqualTo(now);
        assertThat(session.getUpdatedAt()).isEqualTo(now);
    }

    @Test
    void builder_WithEmptyUsersList_ShouldCreateSession() {
        Session session = Session.builder()
                .name("No Users Session")
                .date(new Date())
                .description("Session with no users")
                .users(new ArrayList<>())
                .build();

        assertThat(session.getUsers()).isEmpty();
    }

    @Test
    void builder_WithMultipleUsers_ShouldAddAllUsers() {
        User user1 = User.builder().id(1L).email("u1@test.com").lastName("Doe").firstName("John").password("p").admin(false).build();
        User user2 = User.builder().id(2L).email("u2@test.com").lastName("Smith").firstName("Jane").password("p").admin(false).build();
        User user3 = User.builder().id(3L).email("u3@test.com").lastName("Brown").firstName("Bob").password("p").admin(false).build();

        Session session = Session.builder()
                .name("Popular Session")
                .date(new Date())
                .description("Session with many users")
                .users(Arrays.asList(user1, user2, user3))
                .build();

        assertThat(session.getUsers()).hasSize(3);
        assertThat(session.getUsers()).containsExactly(user1, user2, user3);
    }

    @Test
    void builder_WithTeacher_ShouldSetTeacher() {
        Teacher teacher = Teacher.builder()
                .id(5L)
                .lastName("Wilson")
                .firstName("Emma")
                .build();

        Session session = Session.builder()
                .name("Session with Teacher")
                .date(new Date())
                .description("Description")
                .teacher(teacher)
                .build();

        assertThat(session.getTeacher()).isNotNull();
        assertThat(session.getTeacher().getId()).isEqualTo(5L);
        assertThat(session.getTeacher().getLastName()).isEqualTo("Wilson");
    }

    @Test
    void getters_ShouldReturnCorrectValues() {
        LocalDateTime created = LocalDateTime.now().minusDays(1);
        LocalDateTime updated = LocalDateTime.now();
        Date date = new Date();
        Teacher teacher = Teacher.builder().id(10L).lastName("Teacher").firstName("Test").build();
        List<User> users = Arrays.asList(
            User.builder().id(1L).email("user@test.com").lastName("User").firstName("Test").password("p").admin(false).build()
        );
        
        Session session = Session.builder()
                .id(99L)
                .name("Complete Session")
                .date(date)
                .description("Complete description")
                .teacher(teacher)
                .users(users)
                .createdAt(created)
                .updatedAt(updated)
                .build();

        assertThat(session.getId()).isEqualTo(99L);
        assertThat(session.getName()).isEqualTo("Complete Session");
        assertThat(session.getDate()).isEqualTo(date);
        assertThat(session.getDescription()).isEqualTo("Complete description");
        assertThat(session.getTeacher().getId()).isEqualTo(10L);
        assertThat(session.getUsers()).hasSize(1);
        assertThat(session.getCreatedAt()).isEqualTo(created);
        assertThat(session.getUpdatedAt()).isEqualTo(updated);
    }
}
