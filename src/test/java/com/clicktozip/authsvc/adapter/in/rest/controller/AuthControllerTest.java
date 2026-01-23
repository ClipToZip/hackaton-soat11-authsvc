package com.clicktozip.authsvc.adapter.in.rest.controller;

import com.clicktozip.authsvc.adapter.in.rest.exception.GlobalExceptionHandler;
import com.clicktozip.authsvc.application.port.in.AuthUseCasePort;
import com.clicktozip.authsvc.application.port.in.RegisterUseCasePort;
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

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(MockitoExtension.class)
class AuthControllerTest {

    private MockMvc mockMvc;

    @InjectMocks
    private AuthController authController;

    private final ObjectMapper objectMapper = new ObjectMapper();

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(authController)
                .setControllerAdvice(new GlobalExceptionHandler()) // Important to test exception handling
                .build();
    }

    @Test
    void whenRegisterWithShortPassword_shouldReturnBadRequest() throws Exception {
        Map<String, String> request = Map.of(
                "email", "test@example.com",
                "password", "12345" // Short password
        );

        mockMvc.perform(post("/auth/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message").value("password: A senha deve ter no mínimo 6 caracteres"));
    }

    @Test
    void whenRegisterWithInvalidEmail_shouldReturnBadRequest() throws Exception {
        Map<String, String> request = Map.of(
                "email", "not-an-email",
                "password", "password123"
        );

        mockMvc.perform(post("/auth/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message").value("email: Email deve ser válido"));
    }

    @Test
    void whenRegisterWithBlankPassword_shouldReturnBadRequest() throws Exception {
        Map<String, String> request = Map.of(
                "email", "test@example.com",
                "password", ""
        );

        mockMvc.perform(post("/auth/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message").isString());
    }
}
