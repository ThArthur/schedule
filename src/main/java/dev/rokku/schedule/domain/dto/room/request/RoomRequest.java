package dev.rokku.schedule.domain.dto.room.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record RoomRequest(
    String floor,
    @NotBlank String number,
    @NotNull Long buildingId
) {}
