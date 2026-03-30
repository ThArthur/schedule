package dev.rokku.schedule.domain.dto.room.response;

import java.time.OffsetDateTime;

public record RoomResponse(
    Long id,
    String floor,
    String number,
    Long buildingId,
    String imageUrl,
    OffsetDateTime createdAt,
    OffsetDateTime updatedAt
) {}
