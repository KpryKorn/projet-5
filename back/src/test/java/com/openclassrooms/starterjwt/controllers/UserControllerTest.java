package com.openclassrooms.starterjwt.controllers;

import com.openclassrooms.starterjwt.dto.UserDto;
import com.openclassrooms.starterjwt.mapper.UserMapper;
import com.openclassrooms.starterjwt.models.User;
import com.openclassrooms.starterjwt.services.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDateTime;

import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
class UserControllerTest {

    private final MockMvc mockMvc;

    @MockBean
    private UserService userService;

    @MockBean
    private UserMapper userMapper;

    @Autowired
    public UserControllerTest(MockMvc mockMvc) {
        this.mockMvc = mockMvc;
    }

    private User testUser;
    private UserDto testUserDto;

    @BeforeEach
    void setUp() {
        testUser = User.builder()
                .id(1L)
                .email("test@test.com")
                .firstName("Test")
                .lastName("User")
                .password("password")
                .admin(false)
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .build();

        testUserDto = new UserDto();
        testUserDto.setId(1L);
        testUserDto.setEmail("test@test.com");
        testUserDto.setFirstName("Test");
        testUserDto.setLastName("User");
        testUserDto.setAdmin(false);
    }

    @Test
    @WithMockUser(username = "test@test.com")
    void findById_WithValidId_ShouldReturnUser() throws Exception {
        when(userService.findById(1L)).thenReturn(testUser);
        when(userMapper.toDto(testUser)).thenReturn(testUserDto);

        mockMvc.perform(get("/api/user/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.email").value("test@test.com"))
                .andExpect(jsonPath("$.firstName").value("Test"))
                .andExpect(jsonPath("$.lastName").value("User"));

        verify(userService, times(1)).findById(1L);
        verify(userMapper, times(1)).toDto(testUser);
    }

    @Test
    @WithMockUser(username = "test@test.com")
    void findById_WithNonExistentId_ShouldReturnNotFound() throws Exception {
        when(userService.findById(999L)).thenReturn(null);

        mockMvc.perform(get("/api/user/999"))
                .andExpect(status().isNotFound());

        verify(userService, times(1)).findById(999L);
    }

    @Test
    @WithMockUser(username = "test@test.com")
    void findById_WithInvalidId_ShouldReturnBadRequest() throws Exception {
        mockMvc.perform(get("/api/user/invalid"))
                .andExpect(status().isBadRequest());

        verify(userService, never()).findById(anyLong());
    }

    @Test
    @WithMockUser(username = "test@test.com")
    void deleteUser_WhenUserIsOwner_ShouldReturnOk() throws Exception {
        when(userService.findById(1L)).thenReturn(testUser);
        doNothing().when(userService).delete(1L);

        mockMvc.perform(delete("/api/user/1"))
                .andExpect(status().isOk());

        verify(userService, times(1)).findById(1L);
        verify(userService, times(1)).delete(1L);
    }

    @Test
    @WithMockUser(username = "other@test.com")
    void deleteUser_WhenUserIsNotOwner_ShouldReturnUnauthorized() throws Exception {
        when(userService.findById(1L)).thenReturn(testUser);

        mockMvc.perform(delete("/api/user/1"))
                .andExpect(status().isUnauthorized());

        verify(userService, times(1)).findById(1L);
        verify(userService, never()).delete(anyLong());
    }

    @Test
    @WithMockUser(username = "test@test.com")
    void deleteUser_WithNonExistentId_ShouldReturnNotFound() throws Exception {
        when(userService.findById(999L)).thenReturn(null);

        mockMvc.perform(delete("/api/user/999"))
                .andExpect(status().isNotFound());

        verify(userService, times(1)).findById(999L);
        verify(userService, never()).delete(anyLong());
    }

    @Test
    @WithMockUser(username = "test@test.com")
    void deleteUser_WithInvalidId_ShouldReturnBadRequest() throws Exception {
        mockMvc.perform(delete("/api/user/invalid"))
                .andExpect(status().isBadRequest());

        verify(userService, never()).findById(anyLong());
        verify(userService, never()).delete(anyLong());
    }
}
