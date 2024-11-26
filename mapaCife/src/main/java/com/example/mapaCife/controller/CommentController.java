package com.example.mapaCife.controller;

import com.example.mapaCife.dto.CommentDTO;
import com.example.mapaCife.dto.CommentMapper;
import com.example.mapaCife.dto.CreateCommentDTO;
import com.example.mapaCife.models.Comment;
import com.example.mapaCife.models.TouristicSpot;
import com.example.mapaCife.models.User;
import com.example.mapaCife.repository.CommentRepository;
import com.example.mapaCife.repository.TouristicSpotRepository;
import com.example.mapaCife.service.CommentService;

import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

@RestController
@RequestMapping("api")
@Validated
public class CommentController {

  @Autowired
  private CommentService commentService;

  @Autowired
  private CommentRepository commentRepository;

  @Autowired
  private TouristicSpotRepository touristicSpotRepository;

  @PostMapping("/touristic-spots/{slug}/comments")
  public ResponseEntity<?> createComment(@PathVariable String slug, @RequestBody CreateCommentDTO dto) {
    User authenticatedUser = getAuthenticatedUser();
    if (authenticatedUser == null) {
      ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Failed to read authentication details");
    }

    TouristicSpot touristicSpot = touristicSpotRepository.findBySlug(slug);
    if (touristicSpot == null) {
      return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Touristic Spot not found!");
    }

    Comment comment = commentService.createComment(dto, authenticatedUser, touristicSpot);
    CommentDTO response = CommentMapper.toDto(comment);
    return ResponseEntity.status(HttpStatus.CREATED).body(response);
  }

  @DeleteMapping("/touristic-spots/{slug}/comments/{id}")
  public ResponseEntity<?> deleteComment(@PathVariable String slug, @PathVariable UUID id) {
    UserDetails authenticatedUser = getAuthenticatedUser();
    if (authenticatedUser == null) {
      ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Failed to read authentication details");
    }

    TouristicSpot touristicSpot = touristicSpotRepository.findBySlug(slug);
    if (touristicSpot == null) {
      return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Touristic Spot not found!");
    }

    Comment comment = commentRepository.findByExternalId(id);
    if (comment == null) {
      return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Comment not found!");
    }

    UserDetails commentAuthor = comment.getAuthor();
    if (!commentAuthor.getUsername().equals(authenticatedUser.getUsername())) {
      return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
    }

    commentRepository.deleteById(comment.getId());
    return ResponseEntity.noContent().build();
  }

  private User getAuthenticatedUser() {
    Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
    Object principal = authentication.getPrincipal();
    if (principal instanceof User) {
      return (User) principal;
    } else {
      return null;
    }
  }
}
