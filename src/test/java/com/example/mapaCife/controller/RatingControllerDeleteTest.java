package com.example.mapaCife.controller;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.ArrayList;
import java.util.Date;
import java.util.UUID;

import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import com.example.mapaCife.config.SecurityConfiguration;
import com.example.mapaCife.models.Rating;
import com.example.mapaCife.models.TouristicSpot;
import com.example.mapaCife.models.User;
import com.example.mapaCife.models.UserRole;
import com.example.mapaCife.repository.RatingRepository;
import com.example.mapaCife.repository.TouristicSpotRepository;
import com.example.mapaCife.repository.UserRepository;
import com.example.mapaCife.service.RatingService;
import com.example.mapaCife.service.TokenService;

@RunWith(SpringRunner.class)
@WebMvcTest(RatingController.class)
@Import({ SecurityConfiguration.class, TokenService.class })
public class RatingControllerDeleteTest {

  @Autowired
  private MockMvc mockMvc;

  @MockBean
  private TouristicSpotRepository touristicSpotRepository;

  @MockBean
  private RatingService ratingService;

  @MockBean
  private RatingRepository ratingRepository;

  @MockBean
  private UserRepository userRepository;

  @Autowired
  private TokenService tokenService;

  @Test
  public void testDeleteRating_Success() throws Exception {
    // Arrange
    User user = new User();
    user.setUsername("test-username");
    user.setRole(UserRole.USER);
    String token = tokenService.generateToken(user);

    TouristicSpot mockTouristicSpot = new TouristicSpot();
    mockTouristicSpot.setId(Long.valueOf(1));
    mockTouristicSpot.setName("Recife Antigo");
    String slug = mockTouristicSpot.getName().replace(" ", "-").toLowerCase();
    mockTouristicSpot.setSlug(slug);
    mockTouristicSpot.setDescription("Descricao do Recife Antigo");
    mockTouristicSpot.setGmapsLink("https://maps.app.goo.gl/Hr842W9gABWKpdxm6");
    mockTouristicSpot.setCreatedAt(new Date());
    mockTouristicSpot.setUpdatedAt(new Date());
    mockTouristicSpot.setPaid(false);

    Rating mockRating = new Rating();
    mockRating.setId(Long.valueOf(1));
    mockRating.setRating(4.3);
    mockRating.setCreatedAt(new Date());
    mockRating.setExternalId(UUID.randomUUID());
    mockRating.setTouristicSpot(mockTouristicSpot);
    mockRating.setAuthor(user);

    Mockito.when(userRepository.findByUsername(user.getUsername())).thenReturn(user);
    Mockito.when(touristicSpotRepository.findBySlug(slug)).thenReturn(mockTouristicSpot);
    Mockito.when(ratingRepository.findByExternalId(mockRating.getExternalId())).thenReturn(mockRating);
    Mockito.doNothing().when(ratingRepository).deleteById(mockRating.getId());

    // Act
    ResultActions result = mockMvc
        .perform(
            delete(String.format("/api/touristic-spots/%s/ratings/%s", slug, mockRating.getExternalId().toString()))
                .header("Authorization", "Bearer " + token));

    // Assert
    result
        .andExpect(status().isNoContent());
  }

  @Test
  public void testDeleteRating_FailTouristicSpotNotFound() throws Exception {
    // Arrange
    User user = new User();
    user.setUsername("test-username");
    user.setRole(UserRole.USER);
    String token = tokenService.generateToken(user);

    String slug = "recife-antigo";

    Mockito.when(userRepository.findByUsername(user.getUsername())).thenReturn(user);
    Mockito.when(touristicSpotRepository.findBySlug(slug)).thenReturn(null);

    // Act
    ResultActions result = mockMvc
        .perform(
            delete(String.format("/api/touristic-spots/%s/ratings/%s", slug, UUID.randomUUID().toString()))
                .header("Authorization", "Bearer " + token));

    // Assert
    result
        .andExpect(status().isNotFound());
  }

  @Test
  public void testDeleteRating_FailRatingNotFound() throws Exception {
    // Arrange
    User user = new User();
    user.setUsername("test-username");
    user.setRole(UserRole.USER);
    String token = tokenService.generateToken(user);

    TouristicSpot mockTouristicSpot = new TouristicSpot();
    mockTouristicSpot.setId(Long.valueOf(1));
    mockTouristicSpot.setName("Recife Antigo");
    String slug = mockTouristicSpot.getName().replace(" ", "-").toLowerCase();
    mockTouristicSpot.setSlug(slug);
    mockTouristicSpot.setDescription("Descricao do Recife Antigo");
    mockTouristicSpot.setGmapsLink("https://maps.app.goo.gl/Hr842W9gABWKpdxm6");
    mockTouristicSpot.setCreatedAt(new Date());
    mockTouristicSpot.setUpdatedAt(new Date());
    mockTouristicSpot.setPaid(false);

    UUID ratingExternalId = UUID.randomUUID();

    Mockito.when(userRepository.findByUsername(user.getUsername())).thenReturn(user);
    Mockito.when(touristicSpotRepository.findBySlug(slug)).thenReturn(mockTouristicSpot);
    Mockito.when(ratingRepository.findByExternalId(ratingExternalId)).thenReturn(null);

    // Act
    ResultActions result = mockMvc
        .perform(
            delete(String.format("/api/touristic-spots/%s/ratings/%s", slug, ratingExternalId.toString()))
                .header("Authorization", "Bearer " + token));

    // Assert
    result
        .andExpect(status().isNotFound());
  }

  @Test
  public void testDeleteRating_FailDifferentRatingAuthor() throws Exception {
    // Arrange
    User user = new User();
    user.setUsername("test-username");
    user.setRole(UserRole.USER);
    String token = tokenService.generateToken(user);

    User ratingAuthor = new User();
    ratingAuthor.setUsername("rating-author");

    TouristicSpot mockTouristicSpot = new TouristicSpot();
    mockTouristicSpot.setId(Long.valueOf(1));
    mockTouristicSpot.setName("Recife Antigo");
    String slug = mockTouristicSpot.getName().replace(" ", "-").toLowerCase();
    mockTouristicSpot.setSlug(slug);
    mockTouristicSpot.setDescription("Descricao do Recife Antigo");
    mockTouristicSpot.setGmapsLink("https://maps.app.goo.gl/Hr842W9gABWKpdxm6");
    mockTouristicSpot.setCreatedAt(new Date());
    mockTouristicSpot.setUpdatedAt(new Date());
    mockTouristicSpot.setPaid(false);

    Rating mockRating = new Rating();
    mockRating.setId(Long.valueOf(1));
    mockRating.setRating(4.3);
    mockRating.setCreatedAt(new Date());
    mockRating.setExternalId(UUID.randomUUID());
    mockRating.setTouristicSpot(mockTouristicSpot);
    mockRating.setAuthor(ratingAuthor);

    Mockito.when(userRepository.findByUsername(user.getUsername())).thenReturn(user);
    Mockito.when(touristicSpotRepository.findBySlug(slug)).thenReturn(mockTouristicSpot);
    Mockito.when(ratingRepository.findByExternalId(mockRating.getExternalId())).thenReturn(mockRating);

    // Act
    ResultActions result = mockMvc
        .perform(
            delete(String.format("/api/touristic-spots/%s/ratings/%s", slug, mockRating.getExternalId().toString()))
                .header("Authorization", "Bearer " + token));

    // Assert
    result
        .andExpect(status().isForbidden());
  }
}
