package com.example.mapaCife.scheduler;

import static org.junit.Assert.assertEquals;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.ThreadLocalRandom;

import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.test.context.junit4.SpringRunner;

import com.example.mapaCife.models.Rating;
import com.example.mapaCife.models.TouristicSpot;
import com.example.mapaCife.repository.RatingRepository;
import com.example.mapaCife.repository.TouristicSpotRepository;

@RunWith(SpringRunner.class)
@SpringBootTest
public class CalculateAverateRatingsTest {

  @Mock
  private TouristicSpotRepository touristicSpotRepository;

  @Mock
  private RatingRepository ratingRepository;

  @InjectMocks
  private CalculateAverageRatings scheduledTask;

  @Test
  public void testCalculateAverageRatings() throws Exception {
    Page<TouristicSpot> touristicSpots = generateNTouristicSpots(50, 0);
    TouristicSpot expectedTouristicSpot = touristicSpots.getContent().get(0);
    List<Rating> ratings = generateNRatings(50, expectedTouristicSpot);

    Double total = Double.valueOf(0);
    for (Rating rating : ratings) {
      total += rating.getRating();
    }

    Double average = total / ratings.size();

    Mockito.when(touristicSpotRepository.findAll(PageRequest.of(0, 100))).thenReturn(touristicSpots);
    Mockito.when(touristicSpotRepository.findAll(PageRequest.of(1, 100))).thenReturn(new PageImpl<>(new ArrayList<>()));
    Mockito.when(ratingRepository.findByTouristicSpot(expectedTouristicSpot)).thenReturn(ratings);

    scheduledTask.calculateAverageRatings();

    ArgumentCaptor<TouristicSpot> captor = ArgumentCaptor.forClass(TouristicSpot.class);
    Mockito.verify(touristicSpotRepository).save(captor.capture());

    TouristicSpot savedSpot = captor.getValue();
    assertEquals(average.floatValue(), savedSpot.getAveragRating(), 0.05);
  }

  private TouristicSpot createRandomTouristicSpot() {
    TouristicSpot touristicSpot = new TouristicSpot();
    String name = UUID.randomUUID().toString();
    touristicSpot.setId(Long.valueOf(1));
    touristicSpot.setSlug(name);
    touristicSpot.setName(name);
    touristicSpot.setDescription(name);
    touristicSpot.setGmapsLink(String.format("https://%s.com", name));
    touristicSpot.setTypeList(null);
    touristicSpot.setCreatedAt(new Date());
    touristicSpot.setUpdatedAt(new Date());
    touristicSpot.setPaid(false);
    return touristicSpot;
  }

  private Page<TouristicSpot> generateNTouristicSpots(int n, int pageNumber) {
    // Create a list of n random TouristicSpots
    List<TouristicSpot> touristicSpots = new ArrayList<>();
    for (int i = 0; i < n; i++) {
      touristicSpots.add(createRandomTouristicSpot());
    }

    // Create a Pageable object to control pagination (page size of n)
    Pageable pageable = PageRequest.of(pageNumber, n);

    // Return a Page object containing the list of touristic spots
    return new PageImpl<>(touristicSpots, pageable, n);
  }

  private Rating createRandomRating(TouristicSpot touristicSpot) {
    Rating rating = new Rating();
    rating.setId(Long.valueOf(1));
    rating.setRating(generateRandomDouble());
    rating.setCreatedAt(new Date());
    rating.setExternalId(UUID.randomUUID());
    rating.setTouristicSpot(touristicSpot);
    return rating;
  }

  private List<Rating> generateNRatings(int n, TouristicSpot touristicSpot) {
    List<Rating> ratings = new ArrayList<>();
    for (int i = 0; i < n; i++) {
      ratings.add(createRandomRating(touristicSpot));
    }
    return ratings;
  }

  private static double generateRandomDouble() {
    return ThreadLocalRandom.current().nextDouble(0.0, 5.0);
  }
}
