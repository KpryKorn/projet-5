package com.openclassrooms.starterjwt.services;

import com.openclassrooms.starterjwt.models.Teacher;
import com.openclassrooms.starterjwt.repository.TeacherRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class TeacherServiceTest {

    @Mock
    private TeacherRepository teacherRepository;

    @InjectMocks
    private TeacherService teacherService;

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
    }

    @Test
    void findAll_ShouldReturnAllTeachers() {
        Teacher teacher2 = Teacher.builder()
                .id(2L)
                .lastName("Doe")
                .firstName("John")
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .build();
        
        List<Teacher> teachers = Arrays.asList(mockTeacher, teacher2);
        when(teacherRepository.findAll()).thenReturn(teachers);

        List<Teacher> result = teacherService.findAll();

        assertThat(result).hasSize(2);
        assertThat(result.get(0).getLastName()).isEqualTo("Smith");
        assertThat(result.get(1).getLastName()).isEqualTo("Doe");
        verify(teacherRepository, times(1)).findAll();
    }

    @Test
    void findAll_ShouldReturnEmptyList_WhenNoTeachers() {
        when(teacherRepository.findAll()).thenReturn(Collections.emptyList());

        List<Teacher> result = teacherService.findAll();

        assertThat(result).isEmpty();
        verify(teacherRepository, times(1)).findAll();
    }

    @Test
    void findById_ShouldReturnTeacher_WhenExists() {
        when(teacherRepository.findById(1L)).thenReturn(Optional.of(mockTeacher));

        Teacher result = teacherService.findById(1L);

        assertThat(result).isNotNull();
        assertThat(result.getId()).isEqualTo(1L);
        assertThat(result.getFirstName()).isEqualTo("Jane");
        assertThat(result.getLastName()).isEqualTo("Smith");
        verify(teacherRepository, times(1)).findById(1L);
    }

    @Test
    void findById_ShouldReturnNull_WhenNotExists() {
        when(teacherRepository.findById(anyLong())).thenReturn(Optional.empty());

        Teacher result = teacherService.findById(999L);

        assertThat(result).isNull();
        verify(teacherRepository, times(1)).findById(999L);
    }
}
