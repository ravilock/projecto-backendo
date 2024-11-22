package com.example.mapaCife.controller;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.ArrayList;
import java.util.Date;

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
import com.example.mapaCife.models.TouristicSpot;
import com.example.mapaCife.models.User;
import com.example.mapaCife.models.UserRole;
import com.example.mapaCife.repository.TouristicSpotRepository;
import com.example.mapaCife.repository.UserRepository;
import com.example.mapaCife.service.TokenService;
import com.example.mapaCife.service.TouristicSpotService;

@RunWith(SpringRunner.class)
@WebMvcTest(TouristicSpotController.class)
@Import({ SecurityConfiguration.class, TokenService.class })
public class TouristicSpotControllerDeleteTest {

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

  @Test
  public void testDeleteTouristicSpot_Success() throws Exception {
    // Arrange
    User user = new User();
    user.setUsername("test-username");
    user.setRole(UserRole.ADMIN);
    String token = tokenService.generateToken(user);

    ArrayList<String> typeList = new ArrayList<String>();
    typeList.add("batata");
    String touristSpotName = "Recife Antigo";
    String slug = touristSpotName.replace(" ", "-").toLowerCase();

    TouristicSpot mockTouristicSpot = new TouristicSpot();
    mockTouristicSpot.setId(Long.valueOf(1));
    mockTouristicSpot.setSlug(slug);
    mockTouristicSpot.setName(touristSpotName);
    mockTouristicSpot.setDescription("Recife Antigo Descrito");
    mockTouristicSpot.setGmapsLink("https://maps.app.goo.gl/iKKzSWbPVZ3BkRwx7");
    mockTouristicSpot.setTypeList(typeList);
    mockTouristicSpot.setCreatedAt(new Date());
    mockTouristicSpot.setUpdatedAt(new Date());
    mockTouristicSpot.setPaid(false);

    Mockito.when(userRepository.findByUsername(user.getUsername())).thenReturn(user);
    Mockito.when(touristicSpotRepository.findBySlug(slug)).thenReturn(mockTouristicSpot);
    Mockito.doNothing().when(touristicSpotRepository).delete(mockTouristicSpot);

    // Act
    ResultActions result = mockMvc.perform(delete(String.format("/api/touristic-spots/%s", slug))
        .header("Authorization", "Bearer " + token));

    // Assert
    result.andExpect(status().isOk());
  }

  @Test
  public void testDeleteTouristicSpot_FailNotFound() throws Exception {
    // Arrange
    User user = new User();
    user.setUsername("test-username");
    user.setRole(UserRole.ADMIN);
    String token = tokenService.generateToken(user);

    String touristSpotName = "Recife Antigo";
    String slug = touristSpotName.replace(" ", "-").toLowerCase();

    Mockito.when(userRepository.findByUsername(user.getUsername())).thenReturn(user);
    Mockito.when(touristicSpotRepository.findBySlug(slug)).thenReturn(null);

    // Act
    ResultActions result = mockMvc.perform(delete(String.format("/api/touristic-spots/%s", slug))
        .header("Authorization", "Bearer " + token));

    // Assert
    result.andExpect(status().isNotFound());
  }
}
