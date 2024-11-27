package com.example.mapaCife.dto;

import com.example.mapaCife.models.Rating;

public class RattingMapper {
  public static RatingDTO toDto(Rating rating) {
    return new RatingDTO(rating.getExternalId(), rating.getCreatedAt(), rating.getRating(),
        rating.getAuthor().getUsername());
  }
}
