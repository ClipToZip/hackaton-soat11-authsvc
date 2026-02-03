package com.cliptozip.authsvc.adapter.in.rest.controller;

import com.cliptozip.authsvc.adapter.in.rest.exception.GlobalExceptionHandler;
import com.cliptozip.authsvc.application.exception.InvalidTokenException;
import com.cliptozip.authsvc.application.port.in.AuthUseCasePort;
import com.cliptozip.authsvc.application.port.in.RegisterUseCasePort;
import com.cliptozip.authsvc.application.port.in.ValidateTokenUseCasePort;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.Map;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(MockitoExtension.class)
class AuthControllerTest {

    private MockMvc mockMvc;

    @Mock
    private AuthUseCasePort authUseCase;

    @Mock
    private RegisterUseCasePort registerUseCase;

    @Mock
    private ValidateTokenUseCasePort validateTokenUseCase;

    @InjectMocks
    private AuthController authController;

    private final ObjectMapper objectMapper = new ObjectMapper();

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(authController)
                .setControllerAdvice(new GlobalExceptionHandler())
                .build();
    }

    // --- Registration Tests ---
    @Test
    void whenRegisterWithValidRequest_shouldReturnCreated() throws Exception {
        Map<String, String> request = Map.of("name", "Test User", "email", "test@example.com", "password", "password123");
        doNothing().when(registerUseCase).register(any());

        mockMvc.perform(post("/auth/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated());
    }

    // --- Token Validation Tests ---
    @Test
    void whenValidateWithValidToken_shouldReturnOk() throws Exception {
        // Given
        String validToken = "valid-jwt-token";
        when(validateTokenUseCase.validate(validToken)).thenReturn("test@example.com"); // Return email on success

        Map<String, String> request = Map.of("token", validToken);

        // When & Then
        mockMvc.perform(post("/auth/validate-token")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk());
    }

    @Test
    void whenValidateWithInvalidToken_shouldReturnUnauthorized() throws Exception {
        // Given
        String invalidToken = "invalid-jwt-token";
        // Simulate the use case throwing the exception
        when(validateTokenUseCase.validate(invalidToken)).thenThrow(new InvalidTokenException("Token is invalid"));

        Map<String, String> request = Map.of("token", invalidToken);

        // When & Then
        mockMvc.perform(post("/auth/validate-token")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isUnauthorized())
                .andExpect(jsonPath("$.message").value("Token is invalid"));
    }

    @Test
    void whenValidateWithEmptyToken_shouldReturnBadRequest() throws Exception {
        Map<String, String> request = Map.of("token", "");

        mockMvc.perform(post("/auth/validate-token")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest());
    }
}
