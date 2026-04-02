package com.bluemarket.application.response;

import java.time.LocalDateTime;
import java.util.UUID;

public record UserResponse(
        UUID id,
        String name,
        String username,
        String email,
        LocalDateTime createdAt) {
}
