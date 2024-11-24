package com.example.mapaCife.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.mapaCife.models.Rating;

@Repository
public interface RatingRepository extends JpaRepository<Rating, Long> {
}
