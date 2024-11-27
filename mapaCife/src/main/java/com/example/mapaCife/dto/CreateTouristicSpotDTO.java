package com.example.mapaCife.dto;

import org.hibernate.validator.constraints.URL;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public record CreateTouristicSpotDTO(
    @NotNull @NotBlank @Size(min = 2, max = 255) String name,
    @NotNull @NotBlank @Size(min = 2, max = 1000) @URL String gmapsLink,
    @NotNull @NotBlank @Size(min = 2, max = 255) String description,
    @NotNull Boolean paid) {
}
