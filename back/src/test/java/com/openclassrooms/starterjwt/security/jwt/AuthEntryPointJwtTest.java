package com.openclassrooms.starterjwt.security.jwt;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.security.core.AuthenticationException;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Map;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AuthEntryPointJwtTest {

    @InjectMocks
    private AuthEntryPointJwt authEntryPointJwt;

    @Mock
    private HttpServletRequest request;

    @Mock
    private HttpServletResponse response;

    @Mock
    private AuthenticationException authException;

    @Mock
    private ServletOutputStream outputStream;

    @Test
    void commence_ShouldSetUnauthorizedResponse() throws Exception {
        when(authException.getMessage()).thenReturn("Unauthorized access");
        when(request.getServletPath()).thenReturn("/api/test");
        when(response.getOutputStream()).thenReturn(outputStream);

        authEntryPointJwt.commence(request, response, authException);

        verify(response).setContentType(MediaType.APPLICATION_JSON_VALUE);
        verify(response).setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        verify(response).getOutputStream();
        verify(outputStream, atLeastOnce()).write(any(byte[].class), anyInt(), anyInt());
    }

    @Test
    void commence_ShouldWriteErrorResponseBody() throws Exception {
        when(authException.getMessage()).thenReturn("Full authentication is required");
        when(request.getServletPath()).thenReturn("/api/sessions");
        when(response.getOutputStream()).thenReturn(outputStream);

        authEntryPointJwt.commence(request, response, authException);

        verify(response).setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        verify(response).getOutputStream();
    }

    @Test
    void commence_ShouldHandleNullMessage() throws Exception {
        when(authException.getMessage()).thenReturn(null);
        when(request.getServletPath()).thenReturn("/api/test");
        when(response.getOutputStream()).thenReturn(outputStream);

        authEntryPointJwt.commence(request, response, authException);

        verify(response).setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        verify(response).getOutputStream();
    }

    @Test
    void commence_ShouldHandleDifferentPaths() throws Exception {
        when(authException.getMessage()).thenReturn("Unauthorized");
        when(request.getServletPath()).thenReturn("/api/auth/signin");
        when(response.getOutputStream()).thenReturn(outputStream);

        authEntryPointJwt.commence(request, response, authException);

        verify(response).setContentType(MediaType.APPLICATION_JSON_VALUE);
        verify(request).getServletPath();
    }
}
