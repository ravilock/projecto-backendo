package com.example.mapaCife.controller;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.hamcrest.Matchers.is;

import java.util.ArrayList;
import java.util.Date;

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
import com.example.mapaCife.dto.UpdateTouristicSpotDTO;
import com.example.mapaCife.exception.ResourceNotFoundException;
import com.example.mapaCife.models.TouristicSpot;
import com.example.mapaCife.models.User;
import com.example.mapaCife.models.UserRole;
import com.example.mapaCife.repository.TouristicSpotRepository;
import com.example.mapaCife.repository.UserRepository;
import com.example.mapaCife.service.TokenService;
import com.example.mapaCife.service.TouristicSpotService;
import com.fasterxml.jackson.databind.ObjectMapper;

@RunWith(SpringRunner.class)
@WebMvcTest(TouristicSpotController.class)
@Import({ SecurityConfiguration.class, TokenService.class })
public class TouristicSpotControllerUpdateTest {

  @Autowired
  private MockMvc mockMvc;

  @MockBean
  private TouristicSpotRepository touristicSpotRepository;

  @MockBean
  private TouristicSpotService touristicSpotService;

  @MockBean
  private UserRepository userRepository;

  @Autowired
  private TokenService tokenService;

  @Autowired
  private ObjectMapper objectMapper;

  @Test
  public void testUpdateTouristicSpot_Success() throws Exception {
    // Arrange
    User user = new User();
    user.setUsername("test-username");
    user.setRole(UserRole.ADMIN);
    String token = tokenService.generateToken(user);

    String oldSlug = "recife-antigo";
    UpdateTouristicSpotDTO updateTouristicSpotDTO = new UpdateTouristicSpotDTO(
        "Recife Novo",
        "https://maps.app.goo.gl/Hr842W9gABWKpdxm6",
        "Descricao do Recife Antigo",
        false);
    String slug = updateTouristicSpotDTO.name().replace(" ", "-").toLowerCase();

    TouristicSpot mockTouristicSpot = new TouristicSpot();
    mockTouristicSpot.setId(Long.valueOf(1));
    mockTouristicSpot.setSlug(slug);
    mockTouristicSpot.setName(updateTouristicSpotDTO.name());
    mockTouristicSpot.setDescription(updateTouristicSpotDTO.description());
    mockTouristicSpot.setGmapsLink(updateTouristicSpotDTO.gmapsLink());
    mockTouristicSpot.setCreatedAt(new Date());
    mockTouristicSpot.setUpdatedAt(new Date());
    mockTouristicSpot.setPaid(updateTouristicSpotDTO.paid());

    Mockito.when(userRepository.findByUsername(user.getUsername())).thenReturn(user);
    Mockito.when(touristicSpotService.updateTouristicSpot(oldSlug, updateTouristicSpotDTO))
        .thenReturn(mockTouristicSpot);

    // Act
    ResultActions result = mockMvc.perform(put(String.format("/api/touristic-spots/%s", oldSlug))
        .header("Authorization", "Bearer " + token)
        .contentType(MediaType.APPLICATION_JSON)
        .content(objectMapper.writeValueAsString(updateTouristicSpotDTO)));

    // Assert
    result
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.slug", is(mockTouristicSpot.getSlug())))
        .andExpect(jsonPath("$.name", is(mockTouristicSpot.getName())))
        .andExpect(jsonPath("$.description", is(mockTouristicSpot.getDescription())))
        .andExpect(jsonPath("$.gmapsLink", is(mockTouristicSpot.getGmapsLink())))
        .andExpect(jsonPath("$.paid", is(mockTouristicSpot.getPaid())));
  }

  @Test
  public void testUpdateTouristicSpot_FailNotFound() throws Exception {
    // Arrange
    User user = new User();
    user.setUsername("test-username");
    user.setRole(UserRole.ADMIN);
    String token = tokenService.generateToken(user);

    String oldSlug = "recife-antigo";
    UpdateTouristicSpotDTO updateTouristicSpotDTO = new UpdateTouristicSpotDTO(
        "Recife Novo",
        "https://maps.app.goo.gl/Hr842W9gABWKpdxm6",
        "Descricao do Recife Antigo",
        false);

    Mockito.when(userRepository.findByUsername(user.getUsername())).thenReturn(user);
    Mockito.doThrow(new ResourceNotFoundException(oldSlug))
    .when(touristicSpotService)
    .updateTouristicSpot(oldSlug, updateTouristicSpotDTO);

    // Act
    ResultActions result = mockMvc.perform(put(String.format("/api/touristic-spots/%s", oldSlug))
        .header("Authorization", "Bearer " + token)
        .contentType(MediaType.APPLICATION_JSON)
        .content(objectMapper.writeValueAsString(updateTouristicSpotDTO)));

    // Assert
    result.andExpect(status().isNotFound());
  }
}
