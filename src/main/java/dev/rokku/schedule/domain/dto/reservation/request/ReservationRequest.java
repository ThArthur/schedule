package dev.rokku.schedule.domain.dto.reservation.request;

import dev.rokku.schedule.domain.model.reservation.ReservationStatus;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import java.math.BigDecimal;
import java.time.OffsetDateTime;

public record ReservationRequest(
    @NotNull Long roomId,
    @NotNull Long userId,
    @NotNull OffsetDateTime startAt,
    @NotNull OffsetDateTime endAt,
    @NotNull @Positive BigDecimal totalPrice,
    @NotNull ReservationStatus status
) {}
