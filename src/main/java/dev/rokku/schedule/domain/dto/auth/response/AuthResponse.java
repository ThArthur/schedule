package dev.rokku.schedule.domain.dto.auth.response;

import java.time.Instant;

public record AuthResponse(
        String token,
        String tokenType,
        Instant expiresAt
) {
}
