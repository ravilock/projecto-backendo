package com.example.mapaCife.controller;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;

import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import com.example.mapaCife.config.SecurityConfiguration;
import com.example.mapaCife.models.TouristicSpot;
import com.example.mapaCife.repository.TouristicSpotRepository;
import com.example.mapaCife.repository.UserRepository;
import com.example.mapaCife.service.TokenService;
import com.example.mapaCife.service.TouristicSpotService;

@RunWith(SpringRunner.class)
@WebMvcTest(TouristicSpotController.class)
@Import({ SecurityConfiguration.class, TokenService.class })
public class TouristicSpotControllerListTest {

  @Autowired
  private MockMvc mockMvc;

  @MockBean
  private TouristicSpotRepository touristicSpotRepository;

  @MockBean
  private TouristicSpotService touristicSpotService;

  @MockBean
  private UserRepository userRepository;

  @Test
  public void testListTouristicSpot_Success() throws Exception {
    // Arrange
    List<TouristicSpot> list = new ArrayList<TouristicSpot>();
    for (int i = 0; i < 10; i++) {
      list.add(createRandomTouristicSpot());
    }

    Pageable expectedPagable = PageRequest.of(0, 10);
    Page<TouristicSpot> touristicSpots = new PageImpl<>(list, expectedPagable, list.size());

    Mockito.when(touristicSpotService.getTouristicSpotsByName(null, 0, 10)).thenReturn(touristicSpots);

    // Act
    ResultActions result = mockMvc.perform(get("/api/touristic-spots"));

    // Assert
    result.andExpect(status().isOk());
    for (int i = 0; i < list.size(); i++) {
      TouristicSpot spot = list.get(i);
      result.andExpect(jsonPath("$.[" + i + "].slug").value(spot.getSlug()));
      result.andExpect(jsonPath("$.[" + i + "].name").value(spot.getName()));
      result.andExpect(jsonPath("$.[" + i + "].description").value(spot.getDescription()));
    }
  }

  @Test
  public void testListTouristicSpot_FilteredByName_Success() throws Exception {
    List<TouristicSpot> list = new ArrayList<>();
    String filterName = "praia";

    for (int i = 0; i < 10; i++) {
      TouristicSpot spot = createRandomTouristicSpot();
      if (i % 2 == 0) {
        spot.setName("Praia de" + i);
      } else {
        spot.setName("Monte de " + i);
      }
      list.add(spot);
    }

    Page<TouristicSpot> touristicSpots = new PageImpl<>(
        list.stream().filter((touristicSpot) -> touristicSpot.getName().toLowerCase().contains(filterName)).toList(),
        PageRequest.of(0, 10), list.size());

    Mockito.when(touristicSpotService.getTouristicSpotsByName(filterName, 0, 10)).thenReturn(touristicSpots);

    ResultActions result = mockMvc.perform(get("/api/touristic-spots").param("name", filterName));

    result.andExpect(status().isOk());

    for (int i = 0; i < list.size(); i++) {
      TouristicSpot spot = list.get(i);
      if (spot.getName().contains(filterName)) {
        result.andExpect(jsonPath("$.[" + i + "].name").value(spot.getName()));
      }
    }
  }

  public TouristicSpot createRandomTouristicSpot() {
    TouristicSpot touristicSpot = new TouristicSpot();
    String name = UUID.randomUUID().toString();
    touristicSpot.setId(Long.valueOf(1));
    touristicSpot.setSlug(name);
    touristicSpot.setName(name);
    touristicSpot.setDescription(name);
    touristicSpot.setGmapsLink(String.format("https://%s.com", name));
    touristicSpot.setCreatedAt(new Date());
    touristicSpot.setUpdatedAt(new Date());
    touristicSpot.setPaid(false);
    return touristicSpot;
  }
}
