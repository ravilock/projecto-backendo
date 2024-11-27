package com.example.mapaCife.dto;

import java.util.Date;
import java.util.UUID;

public record RatingDTO(UUID id, Date createdAt, Double rating, String author) {
}
