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
import com.example.mapaCife.models.Comment;
import com.example.mapaCife.models.TouristicSpot;
import com.example.mapaCife.models.User;
import com.example.mapaCife.repository.CommentRepository;
import com.example.mapaCife.repository.TouristicSpotRepository;
import com.example.mapaCife.repository.UserRepository;
import com.example.mapaCife.service.CommentService;
import com.example.mapaCife.service.TokenService;

@RunWith(SpringRunner.class)
@WebMvcTest(CommentController.class)
@Import({ SecurityConfiguration.class, TokenService.class })
public class CommentControllerListTest {

  @Autowired
  private MockMvc mockMvc;

  @MockBean
  private TouristicSpotRepository touristicSpotRepository;

  @MockBean
  private CommentRepository commentRepository;

  @MockBean
  private CommentService commentService;

  @MockBean
  private UserRepository userRepository;

  @Test
  public void testListComments_Success() throws Exception {
    // Arrange
    TouristicSpot expectedTouristicSpot = createRandomTouristicSpot();
    List<Comment> list = new ArrayList<Comment>();
    for (int i = 0; i < 10; i++) {
      list.add(this.createRandomComment());
    }

    Pageable expectedPagable = PageRequest.of(0, 10);
    Page<Comment> comments = new PageImpl<>(list, expectedPagable, list.size());

    Mockito.when(touristicSpotRepository.findBySlug(expectedTouristicSpot.getSlug())).thenReturn(expectedTouristicSpot);
    Mockito.when(commentService.getCommentsByTouristicSpot(expectedTouristicSpot, 0, 10)).thenReturn(comments);

    // Act
    ResultActions result = mockMvc
        .perform(get(String.format("/api/touristic-spots/%s/comments", expectedTouristicSpot.getSlug())));

    // Assert
    result.andExpect(status().isOk());
    for (int i = 0; i < list.size(); i++) {
      Comment comment = list.get(i);
      result.andExpect(jsonPath("$.[" + i + "].id").value(comment.getExternalId().toString()));
      result.andExpect(jsonPath("$.[" + i + "].body").value(comment.getBody()));
      result.andExpect(jsonPath("$.[" + i + "].author").value(comment.getAuthor().getUsername()));
    }
  }

  @Test
  public void testListComments_FailTouristicSpotNotFound() throws Exception {
    // Arrange
    String slug = UUID.randomUUID().toString();
    Mockito.when(touristicSpotRepository.findBySlug(slug)).thenReturn(null);

    // Act
    ResultActions result = mockMvc
        .perform(get(String.format("/api/touristic-spots/%s/comments", slug)));

    // Assert
    result.andExpect(status().isNotFound());
  }

  public Comment createRandomComment() {
    User author = new User();
    author.setUsername(UUID.randomUUID().toString());
    Comment comment = new Comment();
    UUID externalId = UUID.randomUUID();
    comment.setId(Long.valueOf(1));
    comment.setExternalId(externalId);
    comment.setBody(externalId.toString());
    comment.setCreatedAt(new Date());
    comment.setAuthor(author);
    return comment;
  }

  public TouristicSpot createRandomTouristicSpot() {
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
}
