package dev.rokku.schedule.domain.dto.room_price.request;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import java.math.BigDecimal;

public record RoomPriceRequest(
    @NotNull Long roomId,
    @NotNull @Positive BigDecimal value,
    @NotNull @Positive Integer periodHours,
    boolean active
) {}
