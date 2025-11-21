package com.openclassrooms.starterjwt.mapper;

import com.openclassrooms.starterjwt.dto.UserDto;
import com.openclassrooms.starterjwt.models.User;
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
class UserMapperTest {

    private final UserMapper userMapper;

    @Autowired
    public UserMapperTest(UserMapper userMapper) {
        this.userMapper = userMapper;
    }

    @Test
    void toEntity_ShouldMapUserDtoToUser() {
        UserDto userDto = new UserDto();
        userDto.setId(1L);
        userDto.setEmail("test@test.com");
        userDto.setLastName("Doe");
        userDto.setFirstName("John");
        userDto.setPassword("password123");
        userDto.setAdmin(false);
        userDto.setCreatedAt(LocalDateTime.now());
        userDto.setUpdatedAt(LocalDateTime.now());

        User user = userMapper.toEntity(userDto);

        assertThat(user).isNotNull();
        assertThat(user.getId()).isEqualTo(1L);
        assertThat(user.getEmail()).isEqualTo("test@test.com");
        assertThat(user.getLastName()).isEqualTo("Doe");
        assertThat(user.getFirstName()).isEqualTo("John");
        assertThat(user.getPassword()).isEqualTo("password123");
        assertThat(user.isAdmin()).isFalse();
        assertThat(user.getCreatedAt()).isNotNull();
        assertThat(user.getUpdatedAt()).isNotNull();
    }

    @Test
    void toDto_ShouldMapUserToUserDto() {
        User user = User.builder()
                .id(1L)
                .email("test@test.com")
                .lastName("Doe")
                .firstName("John")
                .password("password123")
                .admin(true)
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .build();

        UserDto userDto = userMapper.toDto(user);

        assertThat(userDto).isNotNull();
        assertThat(userDto.getId()).isEqualTo(1L);
        assertThat(userDto.getEmail()).isEqualTo("test@test.com");
        assertThat(userDto.getLastName()).isEqualTo("Doe");
        assertThat(userDto.getFirstName()).isEqualTo("John");
        assertThat(userDto.getPassword()).isEqualTo("password123");
        assertThat(userDto.isAdmin()).isTrue();
        assertThat(userDto.getCreatedAt()).isNotNull();
        assertThat(userDto.getUpdatedAt()).isNotNull();
    }

    @Test
    void toEntity_WithNullDto_ShouldReturnNull() {
        User user = userMapper.toEntity((UserDto) null);

        assertThat(user).isNull();
    }

    @Test
    void toDto_WithNullEntity_ShouldReturnNull() {
        UserDto userDto = userMapper.toDto((User) null);

        assertThat(userDto).isNull();
    }

    @Test
    void toEntityList_ShouldMapUserDtoListToUserList() {
        UserDto userDto1 = new UserDto();
        userDto1.setId(1L);
        userDto1.setEmail("user1@test.com");
        userDto1.setLastName("Doe");
        userDto1.setFirstName("John");
        userDto1.setPassword("password123");
        userDto1.setAdmin(false);

        UserDto userDto2 = new UserDto();
        userDto2.setId(2L);
        userDto2.setEmail("user2@test.com");
        userDto2.setLastName("Smith");
        userDto2.setFirstName("Jane");
        userDto2.setPassword("password456");
        userDto2.setAdmin(true);

        List<UserDto> userDtoList = Arrays.asList(userDto1, userDto2);

        List<User> userList = userMapper.toEntity(userDtoList);

        assertThat(userList).isNotNull();
        assertThat(userList).hasSize(2);
        
        assertThat(userList.get(0).getId()).isEqualTo(1L);
        assertThat(userList.get(0).getEmail()).isEqualTo("user1@test.com");
        assertThat(userList.get(0).getLastName()).isEqualTo("Doe");
        
        assertThat(userList.get(1).getId()).isEqualTo(2L);
        assertThat(userList.get(1).getEmail()).isEqualTo("user2@test.com");
        assertThat(userList.get(1).isAdmin()).isTrue();
    }

    @Test
    void toDtoList_ShouldMapUserListToUserDtoList() {
        User user1 = User.builder()
                .id(1L)
                .email("user1@test.com")
                .lastName("Doe")
                .firstName("John")
                .password("password123")
                .admin(false)
                .build();

        User user2 = User.builder()
                .id(2L)
                .email("user2@test.com")
                .lastName("Smith")
                .firstName("Jane")
                .password("password456")
                .admin(true)
                .build();

        List<User> userList = Arrays.asList(user1, user2);

        List<UserDto> userDtoList = userMapper.toDto(userList);

        assertThat(userDtoList).isNotNull();
        assertThat(userDtoList).hasSize(2);
        
        assertThat(userDtoList.get(0).getId()).isEqualTo(1L);
        assertThat(userDtoList.get(0).getEmail()).isEqualTo("user1@test.com");
        assertThat(userDtoList.get(0).isAdmin()).isFalse();
        
        assertThat(userDtoList.get(1).getId()).isEqualTo(2L);
        assertThat(userDtoList.get(1).getEmail()).isEqualTo("user2@test.com");
        assertThat(userDtoList.get(1).isAdmin()).isTrue();
    }

    @Test
    void toEntityList_WithNullList_ShouldReturnNull() {
        List<User> userList = userMapper.toEntity((List<UserDto>) null);

        assertThat(userList).isNull();
    }

    @Test
    void toDtoList_WithNullList_ShouldReturnNull() {
        List<UserDto> userDtoList = userMapper.toDto((List<User>) null);

        assertThat(userDtoList).isNull();
    }

    @Test
    void toEntity_WithAdminUser_ShouldMapAdminCorrectly() {
        UserDto adminDto = new UserDto();
        adminDto.setId(99L);
        adminDto.setEmail("admin@test.com");
        adminDto.setLastName("Admin");
        adminDto.setFirstName("Super");
        adminDto.setPassword("adminpass");
        adminDto.setAdmin(true);

        User admin = userMapper.toEntity(adminDto);

        assertThat(admin).isNotNull();
        assertThat(admin.isAdmin()).isTrue();
        assertThat(admin.getEmail()).isEqualTo("admin@test.com");
    }
}
