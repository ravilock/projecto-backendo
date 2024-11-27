package com.example.mapaCife.controller;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.hamcrest.Matchers.is;

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
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import com.example.mapaCife.config.SecurityConfiguration;
import com.example.mapaCife.dto.CreateRatingDTO;
import com.example.mapaCife.models.Rating;
import com.example.mapaCife.models.TouristicSpot;
import com.example.mapaCife.models.User;
import com.example.mapaCife.models.UserRole;
import com.example.mapaCife.repository.RatingRepository;
import com.example.mapaCife.repository.TouristicSpotRepository;
import com.example.mapaCife.repository.UserRepository;
import com.example.mapaCife.service.RatingService;
import com.example.mapaCife.service.TokenService;
import com.fasterxml.jackson.databind.ObjectMapper;

@RunWith(SpringRunner.class)
@WebMvcTest(RatingController.class)
@Import({ SecurityConfiguration.class, TokenService.class })
public class RatingControllerCreateTest {

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

  @Autowired
  private ObjectMapper objectMapper;

  @Test
  public void testCreateRating_Success() throws Exception {
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

    CreateRatingDTO createRatingDTO = new CreateRatingDTO(4.3);
    Rating mockRating = new Rating();
    mockRating.setId(Long.valueOf(1));
    mockRating.setRating(createRatingDTO.rating());
    mockRating.setCreatedAt(new Date());
    mockRating.setExternalId(UUID.randomUUID());
    mockRating.setTouristicSpot(mockTouristicSpot);
    mockRating.setAuthor(user);

    Mockito.when(userRepository.findByUsername(user.getUsername())).thenReturn(user);
    Mockito.when(touristicSpotRepository.findBySlug(slug)).thenReturn(mockTouristicSpot);
    Mockito.when(ratingRepository.findByAuthorAndTouristicSpot(user, mockTouristicSpot)).thenReturn(null);
    Mockito.when(ratingService.createRating(createRatingDTO, user, mockTouristicSpot)).thenReturn(mockRating);

    // Act
    ResultActions result = mockMvc.perform(post(String.format("/api/touristic-spots/%s/ratings", slug))
        .header("Authorization", "Bearer " + token)
        .contentType(MediaType.APPLICATION_JSON)
        .content(objectMapper.writeValueAsString(createRatingDTO)));

    // Assert
    result
        .andExpect(status().isCreated())
        .andExpect(jsonPath("$.id", is(mockRating.getExternalId().toString())))
        .andExpect(jsonPath("$.rating", is(mockRating.getRating())))
        .andExpect(jsonPath("$.author", is(mockRating.getAuthor().getUsername())));
  }

  @Test
  public void testCreateRating_FailTouristicSpotNotFound() throws Exception {
    // Arrange
    User user = new User();
    user.setUsername("test-username");
    user.setRole(UserRole.USER);
    String token = tokenService.generateToken(user);

    String slug = "recife-antigo";

    CreateRatingDTO createRatingDTO = new CreateRatingDTO(4.3);

    Mockito.when(userRepository.findByUsername(user.getUsername())).thenReturn(user);
    Mockito.when(touristicSpotRepository.findBySlug(slug)).thenReturn(null);

    // Act
    ResultActions result = mockMvc.perform(post(String.format("/api/touristic-spots/%s/ratings", slug))
        .header("Authorization", "Bearer " + token)
        .contentType(MediaType.APPLICATION_JSON)
        .content(objectMapper.writeValueAsString(createRatingDTO)));

    // Assert
    result.andExpect(status().isNotFound());
  }

  @Test
  public void testCreateRating_FailUserAlreadyRated() throws Exception {
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

    CreateRatingDTO createRatingDTO = new CreateRatingDTO(4.3);
    Rating mockRating = new Rating();
    mockRating.setId(Long.valueOf(1));
    mockRating.setRating(createRatingDTO.rating());
    mockRating.setCreatedAt(new Date());
    mockRating.setExternalId(UUID.randomUUID());
    mockRating.setTouristicSpot(mockTouristicSpot);
    mockRating.setAuthor(user);
    ArrayList<Rating> ratingList = new ArrayList<Rating>();
    ratingList.add(mockRating);

    Mockito.when(userRepository.findByUsername(user.getUsername())).thenReturn(user);
    Mockito.when(touristicSpotRepository.findBySlug(slug)).thenReturn(mockTouristicSpot);
    Mockito.when(ratingRepository.findByAuthorAndTouristicSpot(user,
        mockTouristicSpot)).thenReturn(ratingList);

    // Act
    ResultActions result = mockMvc.perform(post(String.format("/api/touristic-spots/%s/ratings", slug))
        .header("Authorization", "Bearer " + token)
        .contentType(MediaType.APPLICATION_JSON)
        .content(objectMapper.writeValueAsString(createRatingDTO)));

    // Assert
    result.andExpect(status().isConflict());
  }
}
