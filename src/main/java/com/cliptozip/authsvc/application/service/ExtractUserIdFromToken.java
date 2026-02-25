package com.cliptozip.authsvc.application.service;

import com.cliptozip.authsvc.application.exception.InvalidTokenException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.Base64;

@Service
public class ExtractUserIdFromToken {

    private final ObjectMapper objectMapper = new ObjectMapper();

    public String execute(String token) {
        if (token == null || !token.startsWith("Bearer ")) {
            throw new InvalidTokenException("Token is missing or malformed.");
        }

        String jwt = token.substring(7);

        try {
            String[] parts = jwt.split("\\.");
            if (parts.length < 2) {
                throw new InvalidTokenException("Invalid JWT format.");
            }
            String payload = parts[1];
            byte[] decodedBytes = Base64.getUrlDecoder().decode(payload);
            String decodedPayload = new String(decodedBytes);

            JsonNode payloadNode = objectMapper.readTree(decodedPayload);
            if (payloadNode.has("sub")) {
                return payloadNode.get("sub").asText();
            } else {
                throw new RuntimeException("Token does not contain 'sub' (subject) claim.");
            }

        } catch (IOException | IllegalArgumentException e) {
            throw new RuntimeException("Failed to decode or parse JWT payload: " + e.getMessage());
        }
    }
}
