package dev.rokku.schedule.domain.dto.building.response;

import java.time.OffsetDateTime;

public record BuildingResponse(
    Long id,
    String name,
    String number,
    String complement,
    OffsetDateTime createdAt,
    OffsetDateTime updatedAt
) {}
