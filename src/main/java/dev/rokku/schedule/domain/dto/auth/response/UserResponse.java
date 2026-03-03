package dev.rokku.schedule.domain.dto.auth.response;

import dev.rokku.schedule.domain.model.user.UserRole;

public record UserResponse(
        Long id,
        String name,
        String email,
        UserRole role
) {
}
