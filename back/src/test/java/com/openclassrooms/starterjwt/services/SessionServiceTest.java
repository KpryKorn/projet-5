package com.openclassrooms.starterjwt.services;

import com.openclassrooms.starterjwt.exception.BadRequestException;
import com.openclassrooms.starterjwt.exception.NotFoundException;
import com.openclassrooms.starterjwt.models.Session;
import com.openclassrooms.starterjwt.models.Teacher;
import com.openclassrooms.starterjwt.models.User;
import com.openclassrooms.starterjwt.repository.SessionRepository;
import com.openclassrooms.starterjwt.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class SessionServiceTest {

    @Mock
    private SessionRepository sessionRepository;

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private SessionService sessionService;

    private Session mockSession;
    private User mockUser;
    private Teacher mockTeacher;

    @BeforeEach
    void setUp() {
        mockTeacher = Teacher.builder()
                .id(1L)
                .lastName("Smith")
                .firstName("Jane")
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .build();

        mockUser = User.builder()
                .id(1L)
                .email("user@test.com")
                .lastName("Doe")
                .firstName("John")
                .password("password123")
                .admin(false)
                .build();

        mockSession = Session.builder()
                .id(1L)
                .name("Yoga Session")
                .description("Morning yoga")
                .date(new Date())
                .teacher(mockTeacher)
                .users(new ArrayList<>())
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .build();
    }

    // ===== TESTS UNITAIRES =====

    @Test
    void create_ShouldSaveAndReturnSession() {
        // Given
        when(sessionRepository.save(any(Session.class))).thenReturn(mockSession);

        // When
        Session result = sessionService.create(mockSession);

        // Then
        assertThat(result).isNotNull();
        assertThat(result.getName()).isEqualTo("Yoga Session");
        assertThat(result.getDescription()).isEqualTo("Morning yoga");
        verify(sessionRepository, times(1)).save(mockSession);
    }

    @Test
    void delete_ShouldCallRepositoryDeleteById() {
        // Given
        Long sessionId = 1L;

        // When
        sessionService.delete(sessionId);

        // Then
        verify(sessionRepository, times(1)).deleteById(sessionId);
    }

    @Test
    void findAll_ShouldReturnAllSessions() {
        // Given
        Session session2 = Session.builder()
                .id(2L)
                .name("Pilates Session")
                .description("Evening pilates")
                .date(new Date())
                .teacher(mockTeacher)
                .users(new ArrayList<>())
                .build();
        
        List<Session> sessions = Arrays.asList(mockSession, session2);
        when(sessionRepository.findAll()).thenReturn(sessions);

        // When
        List<Session> result = sessionService.findAll();

        // Then
        assertThat(result).hasSize(2);
        assertThat(result.get(0).getName()).isEqualTo("Yoga Session");
        assertThat(result.get(1).getName()).isEqualTo("Pilates Session");
        verify(sessionRepository, times(1)).findAll();
    }

    @Test
    void findAll_ShouldReturnEmptyList_WhenNoSessions() {
        // Given
        when(sessionRepository.findAll()).thenReturn(new ArrayList<>());

        // When
        List<Session> result = sessionService.findAll();

        // Then
        assertThat(result).isEmpty();
        verify(sessionRepository, times(1)).findAll();
    }

    @Test
    void getById_ShouldReturnSession_WhenExists() {
        // Given
        when(sessionRepository.findById(1L)).thenReturn(Optional.of(mockSession));

        // When
        Session result = sessionService.getById(1L);

        // Then
        assertThat(result).isNotNull();
        assertThat(result.getId()).isEqualTo(1L);
        assertThat(result.getName()).isEqualTo("Yoga Session");
        verify(sessionRepository, times(1)).findById(1L);
    }

    @Test
    void getById_ShouldReturnNull_WhenNotExists() {
        // Given
        when(sessionRepository.findById(anyLong())).thenReturn(Optional.empty());

        // When
        Session result = sessionService.getById(999L);

        // Then
        assertThat(result).isNull();
        verify(sessionRepository, times(1)).findById(999L);
    }

    @Test
    void update_ShouldSaveAndReturnUpdatedSession() {
        // Given
        mockSession.setName("Updated Yoga Session");
        when(sessionRepository.save(any(Session.class))).thenReturn(mockSession);

        // When
        Session result = sessionService.update(1L, mockSession);

        // Then
        assertThat(result).isNotNull();
        assertThat(result.getId()).isEqualTo(1L);
        assertThat(result.getName()).isEqualTo("Updated Yoga Session");
        verify(sessionRepository, times(1)).save(mockSession);
    }

    @Test
    void participate_ShouldAddUserToSession() {
        // Given
        when(sessionRepository.findById(1L)).thenReturn(Optional.of(mockSession));
        when(userRepository.findById(1L)).thenReturn(Optional.of(mockUser));

        // When
        sessionService.participate(1L, 1L);

        // Then
        assertThat(mockSession.getUsers()).contains(mockUser);
        verify(sessionRepository, times(1)).save(mockSession);
    }

    @Test
    void participate_ShouldThrowNotFoundException_WhenSessionNotFound() {
        // Given
        when(sessionRepository.findById(anyLong())).thenReturn(Optional.empty());
        when(userRepository.findById(1L)).thenReturn(Optional.of(mockUser));

        // When & Then
        assertThatThrownBy(() -> sessionService.participate(999L, 1L))
                .isInstanceOf(NotFoundException.class);
        
        verify(sessionRepository, never()).save(any());
    }

    @Test
    void participate_ShouldThrowNotFoundException_WhenUserNotFound() {
        // Given
        when(sessionRepository.findById(1L)).thenReturn(Optional.of(mockSession));
        when(userRepository.findById(anyLong())).thenReturn(Optional.empty());

        // When & Then
        assertThatThrownBy(() -> sessionService.participate(1L, 999L))
                .isInstanceOf(NotFoundException.class);
        
        verify(sessionRepository, never()).save(any());
    }

    @Test
    void participate_ShouldThrowBadRequestException_WhenUserAlreadyParticipating() {
        // Given
        mockSession.getUsers().add(mockUser);
        when(sessionRepository.findById(1L)).thenReturn(Optional.of(mockSession));
        when(userRepository.findById(1L)).thenReturn(Optional.of(mockUser));

        // When & Then
        assertThatThrownBy(() -> sessionService.participate(1L, 1L))
                .isInstanceOf(BadRequestException.class);
        
        verify(sessionRepository, never()).save(any());
    }

    @Test
    void noLongerParticipate_ShouldRemoveUserFromSession() {
        // Given
        mockSession.getUsers().add(mockUser);
        when(sessionRepository.findById(1L)).thenReturn(Optional.of(mockSession));

        // When
        sessionService.noLongerParticipate(1L, 1L);

        // Then
        assertThat(mockSession.getUsers()).doesNotContain(mockUser);
        verify(sessionRepository, times(1)).save(mockSession);
    }

    @Test
    void noLongerParticipate_ShouldThrowNotFoundException_WhenSessionNotFound() {
        // Given
        when(sessionRepository.findById(anyLong())).thenReturn(Optional.empty());

        // When & Then
        assertThatThrownBy(() -> sessionService.noLongerParticipate(999L, 1L))
                .isInstanceOf(NotFoundException.class);
        
        verify(sessionRepository, never()).save(any());
    }

    @Test
    void noLongerParticipate_ShouldThrowBadRequestException_WhenUserNotParticipating() {
        // Given
        when(sessionRepository.findById(1L)).thenReturn(Optional.of(mockSession));

        // When & Then
        assertThatThrownBy(() -> sessionService.noLongerParticipate(1L, 1L))
                .isInstanceOf(BadRequestException.class);
        
        verify(sessionRepository, never()).save(any());
    }
}
