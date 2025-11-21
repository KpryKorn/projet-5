package com.openclassrooms.starterjwt.services;

import com.openclassrooms.starterjwt.models.Teacher;
import com.openclassrooms.starterjwt.repository.TeacherRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import java.time.LocalDateTime;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@ActiveProfiles("test")
class TeacherServiceIntegrationTest {

    private final TeacherService teacherService;
    private final TeacherRepository teacherRepository;

    @Autowired
    public TeacherServiceIntegrationTest(TeacherService teacherService, TeacherRepository teacherRepository) {
        this.teacherService = teacherService;
        this.teacherRepository = teacherRepository;
    }

    @AfterEach
    void tearDown() {
        teacherRepository.deleteAll();
    }

    @Test
    void teacherManagement_ShouldCreateAndRetrieveTeachers() {
        Teacher teacher1 = Teacher.builder()
                .lastName("Smith")
                .firstName("Jane")
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .build();

        Teacher teacher2 = Teacher.builder()
                .lastName("Doe")
                .firstName("John")
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .build();

        Teacher savedTeacher1 = teacherRepository.save(teacher1);
        Teacher savedTeacher2 = teacherRepository.save(teacher2);

        assertThat(savedTeacher1.getId()).isNotNull();
        assertThat(savedTeacher2.getId()).isNotNull();

        List<Teacher> allTeachers = teacherService.findAll();
        
        assertThat(allTeachers).hasSize(2);
        assertThat(allTeachers)
                .extracting(Teacher::getLastName)
                .containsExactlyInAnyOrder("Smith", "Doe");

        Teacher foundTeacher = teacherService.findById(savedTeacher1.getId());
        
        assertThat(foundTeacher).isNotNull();
        assertThat(foundTeacher.getFirstName()).isEqualTo("Jane");
        assertThat(foundTeacher.getLastName()).isEqualTo("Smith");
        assertThat(foundTeacher.getCreatedAt()).isNotNull();
        assertThat(foundTeacher.getUpdatedAt()).isNotNull();
    }
}
