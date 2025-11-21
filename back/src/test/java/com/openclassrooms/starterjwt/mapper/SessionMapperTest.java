package com.openclassrooms.starterjwt.mapper;

import com.openclassrooms.starterjwt.dto.SessionDto;
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
import java.util.Arrays;
import java.util.Date;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@ActiveProfiles("test")
class SessionMapperTest {

    private final SessionMapper sessionMapper;
    private final TeacherRepository teacherRepository;
    private final UserRepository userRepository;
    private final SessionRepository sessionRepository;

    @Autowired
    public SessionMapperTest(
            SessionMapper sessionMapper,
            TeacherRepository teacherRepository,
            UserRepository userRepository,
            SessionRepository sessionRepository) {
        this.sessionMapper = sessionMapper;
        this.teacherRepository = teacherRepository;
        this.userRepository = userRepository;
        this.sessionRepository = sessionRepository;
    }

    private Teacher teacher;
    private User user1;
    private User user2;

    @BeforeEach
    void setUp() {
        teacher = Teacher.builder()
                .lastName("Smith")
                .firstName("Jane")
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .build();
        teacher = teacherRepository.save(teacher);

        user1 = User.builder()
                .email("user1@test.com")
                .lastName("Doe")
                .firstName("John")
                .password("password123")
                .admin(false)
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .build();
        user1 = userRepository.save(user1);

        user2 = User.builder()
                .email("user2@test.com")
                .lastName("Smith")
                .firstName("Jane")
                .password("password456")
                .admin(false)
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .build();
        user2 = userRepository.save(user2);
    }

    @AfterEach
    void tearDown() {
        sessionRepository.deleteAll();
        userRepository.deleteAll();
        teacherRepository.deleteAll();
    }

    @Test
    void toEntity_ShouldMapSessionDtoToSession() {
        SessionDto sessionDto = new SessionDto();
        sessionDto.setId(1L);
        sessionDto.setName("Morning Yoga");
        sessionDto.setDescription("Morning session description");
        sessionDto.setDate(new Date());
        sessionDto.setTeacher_id(teacher.getId());
        sessionDto.setUsers(Arrays.asList(user1.getId(), user2.getId()));
        sessionDto.setCreatedAt(LocalDateTime.now());
        sessionDto.setUpdatedAt(LocalDateTime.now());

        Session session = sessionMapper.toEntity(sessionDto);

        assertThat(session).isNotNull();
        assertThat(session.getId()).isEqualTo(1L);
        assertThat(session.getName()).isEqualTo("Morning Yoga");
        assertThat(session.getDescription()).isEqualTo("Morning session description");
        assertThat(session.getDate()).isNotNull();
        assertThat(session.getTeacher()).isNotNull();
        assertThat(session.getTeacher().getId()).isEqualTo(teacher.getId());
        assertThat(session.getUsers()).hasSize(2);
        assertThat(session.getUsers().get(0).getId()).isEqualTo(user1.getId());
        assertThat(session.getUsers().get(1).getId()).isEqualTo(user2.getId());
    }

    @Test
    void toDto_ShouldMapSessionToSessionDto() {
        Session session = Session.builder()
                .id(1L)
                .name("Evening Yoga")
                .description("Evening session description")
                .date(new Date())
                .teacher(teacher)
                .users(Arrays.asList(user1, user2))
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .build();

        SessionDto sessionDto = sessionMapper.toDto(session);

        assertThat(sessionDto).isNotNull();
        assertThat(sessionDto.getId()).isEqualTo(1L);
        assertThat(sessionDto.getName()).isEqualTo("Evening Yoga");
        assertThat(sessionDto.getDescription()).isEqualTo("Evening session description");
        assertThat(sessionDto.getDate()).isNotNull();
        assertThat(sessionDto.getTeacher_id()).isEqualTo(teacher.getId());
        assertThat(sessionDto.getUsers()).hasSize(2);
        assertThat(sessionDto.getUsers()).containsExactlyInAnyOrder(user1.getId(), user2.getId());
    }

    @Test
    void toEntity_WithNullDto_ShouldReturnNull() {
        Session session = sessionMapper.toEntity((SessionDto) null);

        assertThat(session).isNull();
    }

    @Test
    void toDto_WithNullEntity_ShouldReturnNull() {
        SessionDto sessionDto = sessionMapper.toDto((Session) null);

        assertThat(sessionDto).isNull();
    }

    @Test
    void toEntity_WithNullTeacherId_ShouldMapTeacherAsNull() {
        SessionDto sessionDto = new SessionDto();
        sessionDto.setId(1L);
        sessionDto.setName("No Teacher Session");
        sessionDto.setDescription("Session without teacher");
        sessionDto.setDate(new Date());
        sessionDto.setTeacher_id(null);
        sessionDto.setUsers(new ArrayList<>());

        Session session = sessionMapper.toEntity(sessionDto);

        assertThat(session).isNotNull();
        assertThat(session.getName()).isEqualTo("No Teacher Session");
        assertThat(session.getTeacher()).isNull();
    }

    @Test
    void toEntity_WithEmptyUsersList_ShouldMapEmptyList() {
        SessionDto sessionDto = new SessionDto();
        sessionDto.setId(1L);
        sessionDto.setName("Empty Session");
        sessionDto.setDescription("Session with no users");
        sessionDto.setDate(new Date());
        sessionDto.setTeacher_id(teacher.getId());
        sessionDto.setUsers(new ArrayList<>());

        Session session = sessionMapper.toEntity(sessionDto);

        assertThat(session).isNotNull();
        assertThat(session.getUsers()).isEmpty();
    }

    @Test
    void toEntity_WithNullUsersList_ShouldMapEmptyList() {
        SessionDto sessionDto = new SessionDto();
        sessionDto.setId(1L);
        sessionDto.setName("Null Users Session");
        sessionDto.setDescription("Session with null users list");
        sessionDto.setDate(new Date());
        sessionDto.setTeacher_id(teacher.getId());
        sessionDto.setUsers(null);

        Session session = sessionMapper.toEntity(sessionDto);

        assertThat(session).isNotNull();
        assertThat(session.getUsers()).isEmpty();
    }

    @Test
    void toDto_WithNoUsers_ShouldMapEmptyUsersList() {
        Session session = Session.builder()
                .id(1L)
                .name("No Participants")
                .description("Session without participants")
                .date(new Date())
                .teacher(teacher)
                .users(new ArrayList<>())
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .build();

        SessionDto sessionDto = sessionMapper.toDto(session);

        assertThat(sessionDto).isNotNull();
        assertThat(sessionDto.getUsers()).isEmpty();
    }

    @Test
    void toDto_WithNullUsers_ShouldMapEmptyUsersList() {
        Session session = Session.builder()
                .id(1L)
                .name("Null Users")
                .description("Session with null users")
                .date(new Date())
                .teacher(teacher)
                .users(null)
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .build();

        SessionDto sessionDto = sessionMapper.toDto(session);

        assertThat(sessionDto).isNotNull();
        assertThat(sessionDto.getUsers()).isEmpty();
    }

    @Test
    void toEntityList_ShouldMapSessionDtoListToSessionList() {
        SessionDto sessionDto1 = new SessionDto();
        sessionDto1.setId(1L);
        sessionDto1.setName("Session 1");
        sessionDto1.setDescription("First session");
        sessionDto1.setDate(new Date());
        sessionDto1.setTeacher_id(teacher.getId());
        sessionDto1.setUsers(Arrays.asList(user1.getId()));

        SessionDto sessionDto2 = new SessionDto();
        sessionDto2.setId(2L);
        sessionDto2.setName("Session 2");
        sessionDto2.setDescription("Second session");
        sessionDto2.setDate(new Date());
        sessionDto2.setTeacher_id(teacher.getId());
        sessionDto2.setUsers(Arrays.asList(user2.getId()));

        List<SessionDto> sessionDtoList = Arrays.asList(sessionDto1, sessionDto2);

        List<Session> sessionList = sessionMapper.toEntity(sessionDtoList);

        assertThat(sessionList).isNotNull();
        assertThat(sessionList).hasSize(2);
        assertThat(sessionList.get(0).getName()).isEqualTo("Session 1");
        assertThat(sessionList.get(0).getUsers()).hasSize(1);
        assertThat(sessionList.get(1).getName()).isEqualTo("Session 2");
        assertThat(sessionList.get(1).getUsers()).hasSize(1);
    }

    @Test
    void toDtoList_ShouldMapSessionListToSessionDtoList() {
        Session session1 = Session.builder()
                .id(1L)
                .name("Session 1")
                .description("First session")
                .date(new Date())
                .teacher(teacher)
                .users(Arrays.asList(user1))
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .build();

        Session session2 = Session.builder()
                .id(2L)
                .name("Session 2")
                .description("Second session")
                .date(new Date())
                .teacher(teacher)
                .users(Arrays.asList(user2))
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .build();

        List<Session> sessionList = Arrays.asList(session1, session2);

        List<SessionDto> sessionDtoList = sessionMapper.toDto(sessionList);

        assertThat(sessionDtoList).isNotNull();
        assertThat(sessionDtoList).hasSize(2);
        assertThat(sessionDtoList.get(0).getName()).isEqualTo("Session 1");
        assertThat(sessionDtoList.get(0).getUsers()).hasSize(1);
        assertThat(sessionDtoList.get(1).getName()).isEqualTo("Session 2");
        assertThat(sessionDtoList.get(1).getUsers()).hasSize(1);
    }

    @Test
    void toEntityList_WithNullList_ShouldReturnNull() {
        List<Session> sessionList = sessionMapper.toEntity((List<SessionDto>) null);

        assertThat(sessionList).isNull();
    }

    @Test
    void toDtoList_WithNullList_ShouldReturnNull() {
        List<SessionDto> sessionDtoList = sessionMapper.toDto((List<Session>) null);

        assertThat(sessionDtoList).isNull();
    }

    @Test
    void toEntity_WithSingleUser_ShouldMapCorrectly() {
        SessionDto sessionDto = new SessionDto();
        sessionDto.setName("Single User Session");
        sessionDto.setDescription("Session with one user");
        sessionDto.setDate(new Date());
        sessionDto.setTeacher_id(teacher.getId());
        sessionDto.setUsers(Arrays.asList(user1.getId()));

        Session session = sessionMapper.toEntity(sessionDto);

        assertThat(session).isNotNull();
        assertThat(session.getUsers()).hasSize(1);
        assertThat(session.getUsers().get(0).getEmail()).isEqualTo("user1@test.com");
    }

    @Test
    void toDto_WithMultipleUsers_ShouldMapAllUserIds() {
        Session session = Session.builder()
                .name("Multi User Session")
                .description("Session with multiple users")
                .date(new Date())
                .teacher(teacher)
                .users(Arrays.asList(user1, user2))
                .build();

        SessionDto sessionDto = sessionMapper.toDto(session);

        assertThat(sessionDto).isNotNull();
        assertThat(sessionDto.getUsers()).hasSize(2);
        assertThat(sessionDto.getUsers()).contains(user1.getId(), user2.getId());
    }
}
