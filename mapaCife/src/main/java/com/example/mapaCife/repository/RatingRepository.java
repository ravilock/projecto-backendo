package com.example.mapaCife.repository;

import java.util.List;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Repository;

import com.example.mapaCife.models.Rating;
import com.example.mapaCife.models.TouristicSpot;

@Repository
public interface RatingRepository extends JpaRepository<Rating, Long> {
  Rating findByExternalId(UUID externalId);

  List<Rating> findByAuthorAndTouristicSpot(UserDetails author, TouristicSpot touristicSpot);

  List<Rating> findByTouristicSpot(TouristicSpot touristicSpot);
}
