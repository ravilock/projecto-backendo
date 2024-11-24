package com.example.mapaCife.service;

import org.springframework.stereotype.Service;

import com.example.mapaCife.dto.RatingDTO;
import com.example.mapaCife.models.Rating;
import com.example.mapaCife.models.Rating;
import com.example.mapaCife.repository.RatingRepository;
import com.example.mapaCife.repository.RatingRepository;

@Autowired
public class RatingService {
    private final RatingRepository ratingRepository;

    public RatingService(RatingRepository ratingRepository) {
        this.ratingRepository = ratingRepository;
    }

    public Rating criarRating(RatingDTO dto) {
        Rating rating = new Rating();
        rating.setNota((int) dto.getNotice());
        rating.setAuthor(dto.getAuthor());
        return ratingRepository.save(rating);
    }

    public void deletarRating(Long id) {
        ratingRepository.deleteById(id);
    }
}
