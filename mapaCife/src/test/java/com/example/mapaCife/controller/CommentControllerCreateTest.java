package com.example.mapaCife.controller;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.hamcrest.Matchers.is;

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
import com.example.mapaCife.dto.CreateCommentDTO;
import com.example.mapaCife.models.Comment;
import com.example.mapaCife.models.TouristicSpot;
import com.example.mapaCife.models.User;
import com.example.mapaCife.models.UserRole;
import com.example.mapaCife.repository.CommentRepository;
import com.example.mapaCife.repository.TouristicSpotRepository;
import com.example.mapaCife.repository.UserRepository;
import com.example.mapaCife.service.CommentService;
import com.example.mapaCife.service.TokenService;
import com.fasterxml.jackson.databind.ObjectMapper;

@RunWith(SpringRunner.class)
@WebMvcTest(CommentController.class)
@Import({ SecurityConfiguration.class, TokenService.class })
public class CommentControllerCreateTest {

  @Autowired
  private MockMvc mockMvc;

  @MockBean
  private TouristicSpotRepository touristicSpotRepository;

  @MockBean
  private CommentService commentService;

  @MockBean
  private CommentRepository commentRepository;

  @MockBean
  private UserRepository userRepository;

  @Autowired
  private TokenService tokenService;

  @Autowired
  private ObjectMapper objectMapper;

  @Test
  public void testCreateComment_Success() throws Exception {
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

    CreateCommentDTO createCommentDTO = new CreateCommentDTO("Recife Antigo É Lindo");
    Comment mockComment = new Comment();
    mockComment.setId(Long.valueOf(1));
    mockComment.setBody(createCommentDTO.body());
    mockComment.setCreatedAt(new Date());
    mockComment.setExternalId(UUID.randomUUID());
    mockComment.setTouristicSpot(mockTouristicSpot);
    mockComment.setAuthor(user);

    Mockito.when(userRepository.findByUsername(user.getUsername())).thenReturn(user);
    Mockito.when(touristicSpotRepository.findBySlug(slug)).thenReturn(mockTouristicSpot);
    Mockito.when(commentService.createComment(createCommentDTO, user, mockTouristicSpot)).thenReturn(mockComment);

    // Act
    ResultActions result = mockMvc.perform(post(String.format("/api/touristic-spots/%s/comments", slug))
        .header("Authorization", "Bearer " + token)
        .contentType(MediaType.APPLICATION_JSON)
        .content(objectMapper.writeValueAsString(createCommentDTO)));

    // Assert
    result
        .andExpect(status().isCreated())
        .andExpect(jsonPath("$.id", is(mockComment.getExternalId().toString())))
        .andExpect(jsonPath("$.body", is(mockComment.getBody())))
        .andExpect(jsonPath("$.author", is(mockComment.getAuthor().getUsername())));
  }

  @Test
  public void testCreateComment_FailTouristicSpotNotFound() throws Exception {
    // Arrange
    User user = new User();
    user.setUsername("test-username");
    user.setRole(UserRole.USER);
    String token = tokenService.generateToken(user);

    String slug = "recife-antigo";

    CreateCommentDTO createCommentDTO = new CreateCommentDTO("Recife Antigo É Lindo");

    Mockito.when(userRepository.findByUsername(user.getUsername())).thenReturn(user);
    Mockito.when(touristicSpotRepository.findBySlug(slug)).thenReturn(null);

    // Act
    ResultActions result = mockMvc.perform(post(String.format("/api/touristic-spots/%s/comments", slug))
        .header("Authorization", "Bearer " + token)
        .contentType(MediaType.APPLICATION_JSON)
        .content(objectMapper.writeValueAsString(createCommentDTO)));

    // Assert
    result
        .andExpect(status().isNotFound());
  }
}
