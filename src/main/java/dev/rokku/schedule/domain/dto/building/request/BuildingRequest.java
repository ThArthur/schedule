package dev.rokku.schedule.domain.dto.building.request;

import jakarta.validation.constraints.NotBlank;

public record BuildingRequest(
    @NotBlank String name,
    String number,
    String complement
) {}
