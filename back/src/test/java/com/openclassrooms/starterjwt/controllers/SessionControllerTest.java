package com.openclassrooms.starterjwt.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.openclassrooms.starterjwt.dto.SessionDto;
import com.openclassrooms.starterjwt.mapper.SessionMapper;
import com.openclassrooms.starterjwt.models.Session;
import com.openclassrooms.starterjwt.models.Teacher;
import com.openclassrooms.starterjwt.services.SessionService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
class SessionControllerTest {

    private final MockMvc mockMvc;
    private final ObjectMapper objectMapper;

    @MockBean
    private SessionService sessionService;

    @MockBean
    private SessionMapper sessionMapper;

    @Autowired
    public SessionControllerTest(
            MockMvc mockMvc,
            ObjectMapper objectMapper) {
        this.mockMvc = mockMvc;
        this.objectMapper = objectMapper;
    }

    private Session testSession;
    private SessionDto testSessionDto;
    private Teacher testTeacher;

    @BeforeEach
    void setUp() {
        testTeacher = Teacher.builder()
                .id(1L)
                .firstName("John")
                .lastName("Doe")
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .build();

        testSession = Session.builder()
                .id(1L)
                .name("Yoga Session")
                .date(new Date())
                .description("A relaxing yoga session")
                .teacher(testTeacher)
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .build();

        testSessionDto = new SessionDto();
        testSessionDto.setId(1L);
        testSessionDto.setName("Yoga Session");
        testSessionDto.setDate(new Date());
        testSessionDto.setDescription("A relaxing yoga session");
        testSessionDto.setTeacher_id(1L);
    }

    @Test
    @WithMockUser
    void findById_WithValidId_ShouldReturnSession() throws Exception {
        when(sessionService.getById(1L)).thenReturn(testSession);
        when(sessionMapper.toDto(testSession)).thenReturn(testSessionDto);

        mockMvc.perform(get("/api/session/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.name").value("Yoga Session"))
                .andExpect(jsonPath("$.description").value("A relaxing yoga session"));

        verify(sessionService, times(1)).getById(1L);
        verify(sessionMapper, times(1)).toDto(testSession);
    }

    @Test
    @WithMockUser
    void findById_WithNonExistentId_ShouldReturnNotFound() throws Exception {
        when(sessionService.getById(999L)).thenReturn(null);

        mockMvc.perform(get("/api/session/999"))
                .andExpect(status().isNotFound());

        verify(sessionService, times(1)).getById(999L);
    }

    @Test
    @WithMockUser
    void findById_WithInvalidId_ShouldReturnBadRequest() throws Exception {
        mockMvc.perform(get("/api/session/invalid"))
                .andExpect(status().isBadRequest());

        verify(sessionService, never()).getById(anyLong());
    }

    @Test
    @WithMockUser
    void findAll_ShouldReturnListOfSessions() throws Exception {
        Session session2 = Session.builder()
                .id(2L)
                .name("Meditation Session")
                .date(new Date())
                .description("A peaceful meditation session")
                .teacher(testTeacher)
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .build();

        SessionDto sessionDto2 = new SessionDto();
        sessionDto2.setId(2L);
        sessionDto2.setName("Meditation Session");
        sessionDto2.setDate(new Date());
        sessionDto2.setDescription("A peaceful meditation session");
        sessionDto2.setTeacher_id(1L);

        List<Session> sessions = Arrays.asList(testSession, session2);
        List<SessionDto> sessionDtos = Arrays.asList(testSessionDto, sessionDto2);

        when(sessionService.findAll()).thenReturn(sessions);
        when(sessionMapper.toDto(sessions)).thenReturn(sessionDtos);

        mockMvc.perform(get("/api/session"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(1))
                .andExpect(jsonPath("$[0].name").value("Yoga Session"))
                .andExpect(jsonPath("$[1].id").value(2))
                .andExpect(jsonPath("$[1].name").value("Meditation Session"));

        verify(sessionService, times(1)).findAll();
        verify(sessionMapper, times(1)).toDto(sessions);
    }

    @Test
    @WithMockUser
    void create_WithValidData_ShouldReturnCreatedSession() throws Exception {
        when(sessionMapper.toEntity(any(SessionDto.class))).thenReturn(testSession);
        when(sessionService.create(any(Session.class))).thenReturn(testSession);
        when(sessionMapper.toDto(testSession)).thenReturn(testSessionDto);

        mockMvc.perform(post("/api/session")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(testSessionDto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.name").value("Yoga Session"));

        verify(sessionService, times(1)).create(any(Session.class));
    }

    @Test
    @WithMockUser
    void update_WithValidData_ShouldReturnUpdatedSession() throws Exception {
        testSessionDto.setName("Updated Session");
        testSession.setName("Updated Session");

        when(sessionMapper.toEntity(any(SessionDto.class))).thenReturn(testSession);
        when(sessionService.update(anyLong(), any(Session.class))).thenReturn(testSession);
        when(sessionMapper.toDto(testSession)).thenReturn(testSessionDto);

        mockMvc.perform(put("/api/session/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(testSessionDto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("Updated Session"));

        verify(sessionService, times(1)).update(anyLong(), any(Session.class));
    }

    @Test
    @WithMockUser
    void update_WithInvalidId_ShouldReturnBadRequest() throws Exception {
        mockMvc.perform(put("/api/session/invalid")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(testSessionDto)))
                .andExpect(status().isBadRequest());

        verify(sessionService, never()).update(anyLong(), any(Session.class));
    }

    @Test
    @WithMockUser
    void delete_WithValidId_ShouldReturnOk() throws Exception {
        when(sessionService.getById(1L)).thenReturn(testSession);
        doNothing().when(sessionService).delete(1L);

        mockMvc.perform(delete("/api/session/1"))
                .andExpect(status().isOk());

        verify(sessionService, times(1)).getById(1L);
        verify(sessionService, times(1)).delete(1L);
    }

    @Test
    @WithMockUser
    void delete_WithNonExistentId_ShouldReturnNotFound() throws Exception {
        when(sessionService.getById(999L)).thenReturn(null);

        mockMvc.perform(delete("/api/session/999"))
                .andExpect(status().isNotFound());

        verify(sessionService, times(1)).getById(999L);
        verify(sessionService, never()).delete(anyLong());
    }

    @Test
    @WithMockUser
    void delete_WithInvalidId_ShouldReturnBadRequest() throws Exception {
        mockMvc.perform(delete("/api/session/invalid"))
                .andExpect(status().isBadRequest());

        verify(sessionService, never()).getById(anyLong());
        verify(sessionService, never()).delete(anyLong());
    }

    @Test
    @WithMockUser
    void participate_WithValidIds_ShouldReturnOk() throws Exception {
        doNothing().when(sessionService).participate(1L, 1L);

        mockMvc.perform(post("/api/session/1/participate/1"))
                .andExpect(status().isOk());

        verify(sessionService, times(1)).participate(1L, 1L);
    }

    @Test
    @WithMockUser
    void participate_WithInvalidIds_ShouldReturnBadRequest() throws Exception {
        mockMvc.perform(post("/api/session/invalid/participate/1"))
                .andExpect(status().isBadRequest());

        verify(sessionService, never()).participate(anyLong(), anyLong());
    }

    @Test
    @WithMockUser
    void noLongerParticipate_WithValidIds_ShouldReturnOk() throws Exception {
        doNothing().when(sessionService).noLongerParticipate(1L, 1L);

        mockMvc.perform(delete("/api/session/1/participate/1"))
                .andExpect(status().isOk());

        verify(sessionService, times(1)).noLongerParticipate(1L, 1L);
    }

    @Test
    @WithMockUser
    void noLongerParticipate_WithInvalidIds_ShouldReturnBadRequest() throws Exception {
        mockMvc.perform(delete("/api/session/invalid/participate/1"))
                .andExpect(status().isBadRequest());

        verify(sessionService, never()).noLongerParticipate(anyLong(), anyLong());
    }
}
