package dev.rokku.schedule.domain.dto.room_block.response;

import java.time.OffsetDateTime;

public record RoomBlockResponse(
    Long id,
    Long roomId,
    OffsetDateTime startAt,
    OffsetDateTime endAt,
    String reason,
    String notes,
    OffsetDateTime createdAt
) {}
