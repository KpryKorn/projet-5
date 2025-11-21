package com.openclassrooms.starterjwt.services;

import com.openclassrooms.starterjwt.models.Session;
import com.openclassrooms.starterjwt.models.Teacher;
import com.openclassrooms.starterjwt.models.User;
import com.openclassrooms.starterjwt.repository.SessionRepository;
import com.openclassrooms.starterjwt.repository.TeacherRepository;
import com.openclassrooms.starterjwt.repository.UserRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@ActiveProfiles("test")
class SessionServiceIntegrationTest {

    private final SessionService sessionService;
    private final SessionRepository sessionRepository;
    private final UserRepository userRepository;
    private final TeacherRepository teacherRepository;

    @Autowired
    public SessionServiceIntegrationTest(SessionService sessionService,
                                         SessionRepository sessionRepository,
                                         UserRepository userRepository,
                                         TeacherRepository teacherRepository) {
        this.sessionService = sessionService;
        this.sessionRepository = sessionRepository;
        this.userRepository = userRepository;
        this.teacherRepository = teacherRepository;
    }

    private Teacher teacher;
    private User user;

    @BeforeEach
    void setUp() {
        teacher = Teacher.builder()
                .lastName("Smith")
                .firstName("Jane")
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .build();
        teacher = teacherRepository.save(teacher);

        user = User.builder()
                .email("integration@test.com")
                .lastName("Doe")
                .firstName("John")
                .password("password123")
                .admin(false)
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .build();
        user = userRepository.save(user);
    }

    @AfterEach
    void tearDown() {
        sessionRepository.deleteAll();
        userRepository.deleteAll();
        teacherRepository.deleteAll();
    }

    @Test
    void completeSessionLifecycle_ShouldCreateUpdateAndDelete() {
        Session session = Session.builder()
                .name("Integration Yoga")
                .description("Integration test session")
                .date(new Date())
                .teacher(teacher)
                .users(new ArrayList<>())
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .build();

        Session createdSession = sessionService.create(session);
        assertThat(createdSession.getId()).isNotNull();
        assertThat(createdSession.getName()).isEqualTo("Integration Yoga");

        createdSession.setName("Updated Yoga Session");
        createdSession.setDescription("Updated description");
        
        Session updatedSession = sessionService.update(createdSession.getId(), createdSession);
        assertThat(updatedSession.getName()).isEqualTo("Updated Yoga Session");
        assertThat(updatedSession.getDescription()).isEqualTo("Updated description");

        Session foundSession = sessionService.getById(createdSession.getId());
        assertThat(foundSession).isNotNull();
        assertThat(foundSession.getName()).isEqualTo("Updated Yoga Session");

        sessionService.delete(createdSession.getId());
        
        Session deletedSession = sessionService.getById(createdSession.getId());
        assertThat(deletedSession).isNull();
    }

    @Test
    void userParticipationWorkflow_ShouldAddAndRemoveUser() {
        Session session = Session.builder()
                .name("Participation Test")
                .description("Test participation workflow")
                .date(new Date())
                .teacher(teacher)
                .users(new ArrayList<>())
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .build();

        Session createdSession = sessionService.create(session);
        Long sessionId = createdSession.getId();
        Long userId = user.getId();

        sessionService.participate(sessionId, userId);
        
        Session sessionWithUser = sessionService.getById(sessionId);
        assertThat(sessionWithUser.getUsers()).hasSize(1);
        assertThat(sessionWithUser.getUsers().get(0).getId()).isEqualTo(userId);
        assertThat(sessionWithUser.getUsers().get(0).getEmail()).isEqualTo("integration@test.com");

        sessionService.noLongerParticipate(sessionId, userId);
        
        Session sessionWithoutUser = sessionService.getById(sessionId);
        assertThat(sessionWithoutUser.getUsers()).isEmpty();
    }

    @Test
    void findAll_ShouldReturnAllCreatedSessions() {
        Session session1 = Session.builder()
                .name("Morning Yoga")
                .description("Morning session")
                .date(new Date())
                .teacher(teacher)
                .users(new ArrayList<>())
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .build();

        Session session2 = Session.builder()
                .name("Evening Yoga")
                .description("Evening session")
                .date(new Date())
                .teacher(teacher)
                .users(new ArrayList<>())
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .build();

        sessionService.create(session1);
        sessionService.create(session2);

        List<Session> allSessions = sessionService.findAll();

        assertThat(allSessions).hasSize(2);
        assertThat(allSessions)
                .extracting(Session::getName)
                .containsExactlyInAnyOrder("Morning Yoga", "Evening Yoga");
    }
}
