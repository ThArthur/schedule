package dev.rokku.schedule.domain.dto.reservation.response;

import dev.rokku.schedule.domain.model.reservation.ReservationStatus;
import java.math.BigDecimal;
import java.time.OffsetDateTime;

public record ReservationResponse(
    Long id,
    Long roomId,
    Long userId,
    OffsetDateTime startAt,
    OffsetDateTime endAt,
    BigDecimal totalPrice,
    ReservationStatus status,
    OffsetDateTime createdAt,
    OffsetDateTime updatedAt
) {}
