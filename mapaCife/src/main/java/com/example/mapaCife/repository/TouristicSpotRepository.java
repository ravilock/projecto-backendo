package com.example.mapaCife.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.mapaCife.models.TouristicSpot;

@Repository
public interface TouristicSpotRepository extends JpaRepository<TouristicSpot, Long> {
  TouristicSpot findBySlug(String slug);

  Page<TouristicSpot> findByNameContainingIgnoreCase(String name, Pageable pageable);
}
