package com.example.mapaCife.service;

import java.util.Date;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.mapaCife.dto.CreateRatingDTO;
import com.example.mapaCife.models.Rating;
import com.example.mapaCife.models.TouristicSpot;
import com.example.mapaCife.models.User;
import com.example.mapaCife.repository.RatingRepository;

@Service
public class RatingService {
  @Autowired
  private RatingRepository ratingRepository;

  public Rating createRating(CreateRatingDTO dto, User ratingAuthor, TouristicSpot touristicSpot) {
    Rating rating = new Rating();
    rating.setRating(dto.rating());
    rating.setExternalId(UUID.randomUUID());
    rating.setCreatedAt(new Date());
    rating.setAuthor(ratingAuthor);
    rating.setTouristicSpot(touristicSpot);
    return ratingRepository.save(rating);
  }
}
