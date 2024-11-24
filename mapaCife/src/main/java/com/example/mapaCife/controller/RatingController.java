package com.example.mapaCife.controller;

import com.example.mapaCife.dto.RatingDTO;
import com.example.mapaCife.models.TouristicSpot;
import com.example.mapaCife.repository.TouristicSpotRepository;
import com.example.mapaCife.service.RatingService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/rating")
public class RatingController {
    private final RatingService ratingService;

    public RatingController(RatingService ratingService) {
        this.ratingService = ratingService;
    }

    @Autowired
    private TouristicSpotRepository touristicSpotRepository;

    @PostMapping("/tourist-spots/{slug}/ratings")
    public ResponseEntity<?> criarRating(@PathVariable String slug, @RequestBody RatingDTO dto) {
        TouristicSpot touristicSpot = touristicSpotRepository.findBySlug(slug);
        if (touristicSpot == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Touristic Spot not found!");
        }
        return ResponseEntity.ok(ratingService.criarRating(dto));

    }

    @DeleteMapping("/tourist-spots/{slug}/ratings/{id}")
    public ResponseEntity<?> deletarRating(@PathVariable String slug, @PathVariable Long id) {
        TouristicSpot touristicSpot = touristicSpotRepository.findBySlug(slug);
        if (touristicSpot == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Touristic Spot not found!");
        }
        ratingService.deletarRating(id);
        return ResponseEntity.noContent().build();
    }
}
