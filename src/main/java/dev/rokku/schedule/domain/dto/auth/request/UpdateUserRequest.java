package dev.rokku.schedule.domain.dto.auth.request;

import jakarta.validation.constraints.NotBlank;

public record UpdateUserRequest(
        @NotBlank String name
) {
}
