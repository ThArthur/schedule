package dev.rokku.schedule.domain.dto.room_block.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.time.OffsetDateTime;

public record RoomBlockRequest(
    @NotNull Long roomId,
    @NotNull OffsetDateTime startAt,
    @NotNull OffsetDateTime endAt,
    @NotBlank String reason,
    String notes
) {}
