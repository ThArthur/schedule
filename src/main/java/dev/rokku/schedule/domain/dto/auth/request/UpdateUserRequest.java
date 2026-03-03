package dev.rokku.schedule.domain.dto.auth.request;

import dev.rokku.schedule.domain.model.user.UserRole;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public record UpdateUserRequest(
        @NotBlank String name,
        @NotBlank @Email String email,
        @Size(min = 6, max = 100) String password,
        @NotNull UserRole role
) {
}
