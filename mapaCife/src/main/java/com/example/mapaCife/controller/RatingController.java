package com.example.mapaCife.controller;

import com.example.mapaCife.dto.CreateRatingDTO;
import com.example.mapaCife.dto.RatingDTO;
import com.example.mapaCife.dto.RattingMapper;
import com.example.mapaCife.models.Rating;
import com.example.mapaCife.models.TouristicSpot;
import com.example.mapaCife.models.User;
import com.example.mapaCife.repository.RatingRepository;
import com.example.mapaCife.repository.TouristicSpotRepository;
import com.example.mapaCife.service.RatingService;

import java.util.List;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("api")
public class RatingController {
  @Autowired
  private RatingService ratingService;

  @Autowired
  private RatingRepository ratingRepository;

  @Autowired
  private TouristicSpotRepository touristicSpotRepository;

  @PostMapping("/touristic-spots/{slug}/ratings")
  public ResponseEntity<?> createRating(@PathVariable String slug, @RequestBody CreateRatingDTO dto) {
    User authenticatedUser = getAuthenticatedUser();
    if (authenticatedUser == null) {
      ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Failed to read authentication details");
    }

    TouristicSpot touristicSpot = touristicSpotRepository.findBySlug(slug);
    if (touristicSpot == null) {
      return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Touristic Spot not found!");
    }

    List<Rating> ratings = ratingRepository.findByAuthorAndTouristicSpot(authenticatedUser, touristicSpot);
    if (ratings != null && !ratings.isEmpty()) {
      return ResponseEntity.status(HttpStatus.CONFLICT).body("Touristic Spot already reviewed");
    }

    Rating rating = ratingService.createRating(dto, authenticatedUser, touristicSpot);
    RatingDTO response = RattingMapper.toDto(rating);
    return ResponseEntity.status(HttpStatus.CREATED).body(response);
  }

  @DeleteMapping("/touristic-spots/{slug}/ratings/{id}")
  public ResponseEntity<?> deleteRating(@PathVariable String slug, @PathVariable UUID id) {
    UserDetails authenticatedUser = getAuthenticatedUser();
    if (authenticatedUser == null) {
      ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Failed to read authentication details");
    }

    TouristicSpot touristicSpot = touristicSpotRepository.findBySlug(slug);
    if (touristicSpot == null) {
      return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Touristic Spot not found!");
    }

    Rating rating = ratingRepository.findByExternalId(id);
    if (rating == null) {
      return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Rating not found!");
    }

    UserDetails ratingAuthor = rating.getAuthor();
    if (!ratingAuthor.getUsername().equals(authenticatedUser.getUsername())) {
      return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
    }

    ratingRepository.deleteById(rating.getId());
    return ResponseEntity.noContent().build();
  }

  private User getAuthenticatedUser() {
    Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
    Object principal = authentication.getPrincipal();
    if (principal instanceof User) {
      return (User) principal;
    } else {
      return null;
    }
  }
}
