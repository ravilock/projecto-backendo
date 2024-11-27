package com.example.mapaCife.scheduler;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.example.mapaCife.models.Rating;
import com.example.mapaCife.models.TouristicSpot;
import com.example.mapaCife.repository.RatingRepository;
import com.example.mapaCife.repository.TouristicSpotRepository;

@Component
public class CalculateAverageRatings {
  private final Logger logger = LoggerFactory.getLogger(this.getClass());

  private static final int PAGE_SIZE = 100;

  @Autowired
  private TouristicSpotRepository touristicSpotRepository;

  @Autowired
  private RatingRepository ratingRepository;

  @Scheduled(cron = "* 0 * * * *") // Every hour
  public void calculateAverageRatings() {
    logger.info("Started to calculate average ratings");
    int pageNumber = 0;
    Pageable pageable = PageRequest.of(pageNumber, PAGE_SIZE);

    Page<TouristicSpot> page;
    do {
      logger.debug(String.format("Calculate average ratings - Page %d", pageNumber));
      page = touristicSpotRepository.findAll(pageable);
      if (page == null) {
        break;
      }
      logger.debug(String.format("Calculate average ratings - Found %d spots", page.getSize()));
      for (TouristicSpot spot : page.getContent()) {
        logger.debug(String.format("Calculating Average Rating for spot %s", spot.getName()));
        Double averageRating = calculateAverageRating(spot);
        if (averageRating == null) {
          logger.debug(String.format("No ratings for spot %s", spot.getName()));
          continue;
        }
        logger.debug(String.format("Ratings for spot %s: %f", spot.getName(), averageRating.floatValue()));
        spot.setAverageRating(averageRating.floatValue());

        touristicSpotRepository.save(spot);
      }

      pageable = pageable.next();
    } while (!page.isEmpty());
    logger.info("Finished calculating average ratings");
  }

  private Double calculateAverageRating(TouristicSpot spot) {
    List<Rating> ratings = ratingRepository.findByTouristicSpot(spot);
    if (ratings == null || ratings.isEmpty()) {
      return null;
    }

    // Calculate the average rating
    double total = 0;
    for (Rating rating : ratings) {
      total += rating.getRating();
    }

    return total / ratings.size();
  }
}
