package dev.rokku.schedule.domain.dto.room_price.response;

import java.math.BigDecimal;
import java.time.OffsetDateTime;

public record RoomPriceResponse(
    Long id,
    Long roomId,
    BigDecimal value,
    Integer periodHours,
    boolean active,
    OffsetDateTime createdAt
) {}
