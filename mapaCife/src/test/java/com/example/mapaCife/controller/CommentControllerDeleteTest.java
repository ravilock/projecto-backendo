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
import com.example.mapaCife.models.Comment;
import com.example.mapaCife.models.TouristicSpot;
import com.example.mapaCife.models.User;
import com.example.mapaCife.models.UserRole;
import com.example.mapaCife.repository.CommentRepository;
import com.example.mapaCife.repository.TouristicSpotRepository;
import com.example.mapaCife.repository.UserRepository;
import com.example.mapaCife.service.CommentService;
import com.example.mapaCife.service.TokenService;

@RunWith(SpringRunner.class)
@WebMvcTest(CommentController.class)
@Import({ SecurityConfiguration.class, TokenService.class })
public class CommentControllerDeleteTest {

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

  @Test
  public void testDeleteComment_Success() throws Exception {
    // Arrange
    User user = new User();
    user.setUsername("test-username");
    user.setRole(UserRole.USER);
    String token = tokenService.generateToken(user);

    ArrayList<String> typeList = new ArrayList<String>();
    typeList.add("batata");
    TouristicSpot mockTouristicSpot = new TouristicSpot();
    mockTouristicSpot.setId(Long.valueOf(1));
    mockTouristicSpot.setName("Recife Antigo");
    String slug = mockTouristicSpot.getName().replace(" ", "-").toLowerCase();
    mockTouristicSpot.setSlug(slug);
    mockTouristicSpot.setDescription("Descricao do Recife Antigo");
    mockTouristicSpot.setGmapsLink("https://maps.app.goo.gl/Hr842W9gABWKpdxm6");
    mockTouristicSpot.setTypeList(typeList);
    mockTouristicSpot.setCreatedAt(new Date());
    mockTouristicSpot.setUpdatedAt(new Date());
    mockTouristicSpot.setPaid(false);

    Comment mockComment = new Comment();
    mockComment.setId(Long.valueOf(1));
    mockComment.setBody("Recife Antigo É Lindo");
    mockComment.setCreatedAt(new Date());
    mockComment.setExternalId(UUID.randomUUID());
    mockComment.setTouristicSpot(mockTouristicSpot);
    mockComment.setAuthor(user);

    Mockito.when(userRepository.findByUsername(user.getUsername())).thenReturn(user);
    Mockito.when(touristicSpotRepository.findBySlug(slug)).thenReturn(mockTouristicSpot);
    Mockito.when(commentRepository.findByExternalId(mockComment.getExternalId())).thenReturn(mockComment);
    Mockito.doNothing().when(commentRepository).deleteById(mockComment.getId());

    // Act
    ResultActions result = mockMvc
        .perform(
            delete(String.format("/api/touristic-spots/%s/comments/%s", slug, mockComment.getExternalId().toString()))
                .header("Authorization", "Bearer " + token));

    // Assert
    result
        .andExpect(status().isNoContent());
  }

  @Test
  public void testDeleteComment_FailTouristicSpotNotFound() throws Exception {
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
            delete(String.format("/api/touristic-spots/%s/comments/%s", slug, UUID.randomUUID().toString()))
                .header("Authorization", "Bearer " + token));

    // Assert
    result
        .andExpect(status().isNotFound());
  }

  @Test
  public void testDeleteComment_FailCommentNotFound() throws Exception {
    // Arrange
    User user = new User();
    user.setUsername("test-username");
    user.setRole(UserRole.USER);
    String token = tokenService.generateToken(user);

    ArrayList<String> typeList = new ArrayList<String>();
    typeList.add("batata");
    TouristicSpot mockTouristicSpot = new TouristicSpot();
    mockTouristicSpot.setId(Long.valueOf(1));
    mockTouristicSpot.setName("Recife Antigo");
    String slug = mockTouristicSpot.getName().replace(" ", "-").toLowerCase();
    mockTouristicSpot.setSlug(slug);
    mockTouristicSpot.setDescription("Descricao do Recife Antigo");
    mockTouristicSpot.setGmapsLink("https://maps.app.goo.gl/Hr842W9gABWKpdxm6");
    mockTouristicSpot.setTypeList(typeList);
    mockTouristicSpot.setCreatedAt(new Date());
    mockTouristicSpot.setUpdatedAt(new Date());
    mockTouristicSpot.setPaid(false);

    UUID commentExternalId = UUID.randomUUID();

    Mockito.when(userRepository.findByUsername(user.getUsername())).thenReturn(user);
    Mockito.when(touristicSpotRepository.findBySlug(slug)).thenReturn(mockTouristicSpot);
    Mockito.when(commentRepository.findByExternalId(commentExternalId)).thenReturn(null);

    // Act
    ResultActions result = mockMvc
        .perform(
            delete(String.format("/api/touristic-spots/%s/comments/%s", slug, commentExternalId.toString()))
                .header("Authorization", "Bearer " + token));

    // Assert
    result
        .andExpect(status().isNotFound());
  }

  @Test
  public void testDeleteComment_FailDifferentCommentAuthor() throws Exception {
    // Arrange
    User user = new User();
    user.setUsername("test-username");
    user.setRole(UserRole.USER);
    String token = tokenService.generateToken(user);

    User commentAuthor = new User();
    commentAuthor.setUsername("comment-author");

    ArrayList<String> typeList = new ArrayList<String>();
    typeList.add("batata");
    TouristicSpot mockTouristicSpot = new TouristicSpot();
    mockTouristicSpot.setId(Long.valueOf(1));
    mockTouristicSpot.setName("Recife Antigo");
    String slug = mockTouristicSpot.getName().replace(" ", "-").toLowerCase();
    mockTouristicSpot.setSlug(slug);
    mockTouristicSpot.setDescription("Descricao do Recife Antigo");
    mockTouristicSpot.setGmapsLink("https://maps.app.goo.gl/Hr842W9gABWKpdxm6");
    mockTouristicSpot.setTypeList(typeList);
    mockTouristicSpot.setCreatedAt(new Date());
    mockTouristicSpot.setUpdatedAt(new Date());
    mockTouristicSpot.setPaid(false);

    Comment mockComment = new Comment();
    mockComment.setId(Long.valueOf(1));
    mockComment.setBody("Recife Antigo É Lindo");
    mockComment.setCreatedAt(new Date());
    mockComment.setExternalId(UUID.randomUUID());
    mockComment.setTouristicSpot(mockTouristicSpot);
    mockComment.setAuthor(commentAuthor);

    Mockito.when(userRepository.findByUsername(user.getUsername())).thenReturn(user);
    Mockito.when(touristicSpotRepository.findBySlug(slug)).thenReturn(mockTouristicSpot);
    Mockito.when(commentRepository.findByExternalId(mockComment.getExternalId())).thenReturn(mockComment);

    // Act
    ResultActions result = mockMvc
        .perform(
            delete(String.format("/api/touristic-spots/%s/comments/%s", slug, mockComment.getExternalId().toString()))
                .header("Authorization", "Bearer " + token));

    // Assert
    result
        .andExpect(status().isForbidden());
  }
}
