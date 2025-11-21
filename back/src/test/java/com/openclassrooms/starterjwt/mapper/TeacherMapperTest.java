package com.openclassrooms.starterjwt.mapper;

import com.openclassrooms.starterjwt.dto.TeacherDto;
import com.openclassrooms.starterjwt.models.Teacher;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@ActiveProfiles("test")
class TeacherMapperTest {

    private final TeacherMapper teacherMapper;

    @Autowired
    public TeacherMapperTest(TeacherMapper teacherMapper) {
        this.teacherMapper = teacherMapper;
    }

    @Test
    void toEntity_ShouldMapTeacherDtoToTeacher() {
        TeacherDto teacherDto = new TeacherDto();
        teacherDto.setId(1L);
        teacherDto.setLastName("Smith");
        teacherDto.setFirstName("Jane");
        teacherDto.setCreatedAt(LocalDateTime.now());
        teacherDto.setUpdatedAt(LocalDateTime.now());

        Teacher teacher = teacherMapper.toEntity(teacherDto);

        assertThat(teacher).isNotNull();
        assertThat(teacher.getId()).isEqualTo(1L);
        assertThat(teacher.getLastName()).isEqualTo("Smith");
        assertThat(teacher.getFirstName()).isEqualTo("Jane");
        assertThat(teacher.getCreatedAt()).isNotNull();
        assertThat(teacher.getUpdatedAt()).isNotNull();
    }

    @Test
    void toDto_ShouldMapTeacherToTeacherDto() {
        Teacher teacher = Teacher.builder()
                .id(1L)
                .lastName("Smith")
                .firstName("Jane")
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .build();

        TeacherDto teacherDto = teacherMapper.toDto(teacher);

        assertThat(teacherDto).isNotNull();
        assertThat(teacherDto.getId()).isEqualTo(1L);
        assertThat(teacherDto.getLastName()).isEqualTo("Smith");
        assertThat(teacherDto.getFirstName()).isEqualTo("Jane");
        assertThat(teacherDto.getCreatedAt()).isNotNull();
        assertThat(teacherDto.getUpdatedAt()).isNotNull();
    }

    @Test
    void toEntity_WithNullDto_ShouldReturnNull() {
        Teacher teacher = teacherMapper.toEntity((TeacherDto) null);

        assertThat(teacher).isNull();
    }

    @Test
    void toDto_WithNullEntity_ShouldReturnNull() {
        TeacherDto teacherDto = teacherMapper.toDto((Teacher) null);

        assertThat(teacherDto).isNull();
    }

    @Test
    void toEntityList_ShouldMapTeacherDtoListToTeacherList() {
        TeacherDto teacherDto1 = new TeacherDto();
        teacherDto1.setId(1L);
        teacherDto1.setLastName("Smith");
        teacherDto1.setFirstName("Jane");
        teacherDto1.setCreatedAt(LocalDateTime.now());
        teacherDto1.setUpdatedAt(LocalDateTime.now());

        TeacherDto teacherDto2 = new TeacherDto();
        teacherDto2.setId(2L);
        teacherDto2.setLastName("Johnson");
        teacherDto2.setFirstName("Bob");
        teacherDto2.setCreatedAt(LocalDateTime.now());
        teacherDto2.setUpdatedAt(LocalDateTime.now());

        List<TeacherDto> teacherDtoList = Arrays.asList(teacherDto1, teacherDto2);

        List<Teacher> teacherList = teacherMapper.toEntity(teacherDtoList);

        assertThat(teacherList).isNotNull();
        assertThat(teacherList).hasSize(2);
        
        assertThat(teacherList.get(0).getId()).isEqualTo(1L);
        assertThat(teacherList.get(0).getLastName()).isEqualTo("Smith");
        assertThat(teacherList.get(0).getFirstName()).isEqualTo("Jane");
        
        assertThat(teacherList.get(1).getId()).isEqualTo(2L);
        assertThat(teacherList.get(1).getLastName()).isEqualTo("Johnson");
        assertThat(teacherList.get(1).getFirstName()).isEqualTo("Bob");
    }

    @Test
    void toDtoList_ShouldMapTeacherListToTeacherDtoList() {
        Teacher teacher1 = Teacher.builder()
                .id(1L)
                .lastName("Smith")
                .firstName("Jane")
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .build();

        Teacher teacher2 = Teacher.builder()
                .id(2L)
                .lastName("Johnson")
                .firstName("Bob")
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .build();

        List<Teacher> teacherList = Arrays.asList(teacher1, teacher2);

        List<TeacherDto> teacherDtoList = teacherMapper.toDto(teacherList);

        assertThat(teacherDtoList).isNotNull();
        assertThat(teacherDtoList).hasSize(2);
        
        assertThat(teacherDtoList.get(0).getId()).isEqualTo(1L);
        assertThat(teacherDtoList.get(0).getLastName()).isEqualTo("Smith");
        assertThat(teacherDtoList.get(0).getFirstName()).isEqualTo("Jane");
        
        assertThat(teacherDtoList.get(1).getId()).isEqualTo(2L);
        assertThat(teacherDtoList.get(1).getLastName()).isEqualTo("Johnson");
        assertThat(teacherDtoList.get(1).getFirstName()).isEqualTo("Bob");
    }

    @Test
    void toEntityList_WithNullList_ShouldReturnNull() {
        List<Teacher> teacherList = teacherMapper.toEntity((List<TeacherDto>) null);

        assertThat(teacherList).isNull();
    }

    @Test
    void toDtoList_WithNullList_ShouldReturnNull() {
        List<TeacherDto> teacherDtoList = teacherMapper.toDto((List<Teacher>) null);

        assertThat(teacherDtoList).isNull();
    }

    @Test
    void toEntity_WithOnlyRequiredFields_ShouldMapCorrectly() {
        TeacherDto teacherDto = new TeacherDto();
        teacherDto.setLastName("Brown");
        teacherDto.setFirstName("Alice");

        Teacher teacher = teacherMapper.toEntity(teacherDto);

        assertThat(teacher).isNotNull();
        assertThat(teacher.getLastName()).isEqualTo("Brown");
        assertThat(teacher.getFirstName()).isEqualTo("Alice");
        assertThat(teacher.getId()).isNull();
    }

    @Test
    void toDto_WithCompleteTeacher_ShouldMapAllFields() {
        LocalDateTime now = LocalDateTime.now();
        Teacher teacher = Teacher.builder()
                .id(42L)
                .lastName("Martinez")
                .firstName("Carlos")
                .createdAt(now)
                .updatedAt(now)
                .build();

        TeacherDto teacherDto = teacherMapper.toDto(teacher);

        assertThat(teacherDto).isNotNull();
        assertThat(teacherDto.getId()).isEqualTo(42L);
        assertThat(teacherDto.getLastName()).isEqualTo("Martinez");
        assertThat(teacherDto.getFirstName()).isEqualTo("Carlos");
        assertThat(teacherDto.getCreatedAt()).isEqualTo(now);
        assertThat(teacherDto.getUpdatedAt()).isEqualTo(now);
    }
}
