package com.example.mapaCife.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public record CreateCommentDTO(
    @NotNull @NotBlank @Size(min = 2, max = 255) String body) {
}
