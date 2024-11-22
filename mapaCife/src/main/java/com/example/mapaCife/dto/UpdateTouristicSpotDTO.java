package com.example.mapaCife.dto;

import java.util.List;

import org.hibernate.validator.constraints.URL;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record UpdateTouristicSpotDTO(
    @NotBlank @Size(min = 2, max = 255) String name,
    @NotBlank @Size(min = 2, max = 1000) @URL String gmapsLink,
    @NotBlank @Size(min = 2, max = 255) String description,
    @Size(min = 1, max = 3) List<String> typeList,
    Boolean paid) {
}
