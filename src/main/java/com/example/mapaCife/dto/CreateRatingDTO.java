package com.example.mapaCife.dto;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;

public record CreateRatingDTO(
    @NotNull @Min(0) @Max(5) Double rating) {
}
