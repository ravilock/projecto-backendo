package com.example.mapaCife.controller;

import com.example.mapaCife.dto.CommentDTO;
import com.example.mapaCife.dto.CommentMapper;
import com.example.mapaCife.dto.CreateCommentDTO;
import com.example.mapaCife.exception.OperationNotAllowedException;
import com.example.mapaCife.exception.ResourceNotFoundException;
import com.example.mapaCife.models.Comment;
import com.example.mapaCife.models.TouristicSpot;
import com.example.mapaCife.models.User;
import com.example.mapaCife.repository.CommentRepository;
import com.example.mapaCife.repository.TouristicSpotRepository;
import com.example.mapaCife.service.CommentService;

import jakarta.validation.Valid;

import java.util.List;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;

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
  @Operation(summary = "Comment touristic spot endpoint", method = "POST")
  @ApiResponses(value = {
      @ApiResponse(responseCode = "201", description = "Successfully commented on touristic spot", content = {
          @Content(mediaType = "application/json", schema = @Schema(implementation = CommentDTO.class)) })
  })
  public ResponseEntity<?> createComment(@PathVariable String slug, @RequestBody @Valid CreateCommentDTO dto) {
    User authenticatedUser = getAuthenticatedUser();
    if (authenticatedUser == null) {
      throw new RuntimeException("Failed to read authentication details");
    }

    TouristicSpot touristicSpot = touristicSpotRepository.findBySlug(slug);
    if (touristicSpot == null) {
      throw new ResourceNotFoundException(slug);
    }

    Comment comment = commentService.createComment(dto, authenticatedUser, touristicSpot);
    CommentDTO response = CommentMapper.toDTO(comment);
    return ResponseEntity.status(HttpStatus.CREATED).body(response);
  }

  @GetMapping("/touristic-spots/{slug}/comments")
  @Operation(summary = "Get touristic spot comment", method = "GET")
  @ApiResponses(value = {
      @ApiResponse(responseCode = "200", description = "Touristic spot comment", content = {
          @Content(mediaType = "application/json", schema = @Schema(implementation = CommentDTO.class)) })
  })
  public ResponseEntity<?> listTouristicSpotComments(
      @PathVariable String slug,
      @RequestParam(defaultValue = "1") int page,
      @RequestParam(defaultValue = "10") int size) {
    TouristicSpot touristicSpot = touristicSpotRepository.findBySlug(slug);
    if (touristicSpot == null) {
      throw new ResourceNotFoundException(slug);
    }
    Page<Comment> comments = commentService.getCommentsByTouristicSpot(touristicSpot, page - 1, size);
    List<CommentDTO> response = CommentMapper.toDTOList(comments.getContent());
    return ResponseEntity.status(HttpStatus.OK).body(response);
  }

  @DeleteMapping("/touristic-spots/{slug}/comments/{id}")
  @Operation(summary = "Delete touristic spot comment", method = "DELETE")
  @ApiResponses(value = {
      @ApiResponse(responseCode = "204", description = "Touristic spot comment")
  })
  public ResponseEntity<?> deleteComment(@PathVariable String slug, @PathVariable UUID id) {
    UserDetails authenticatedUser = getAuthenticatedUser();
    if (authenticatedUser == null) {
      throw new RuntimeException("Failed to read authentication details");
    }

    TouristicSpot touristicSpot = touristicSpotRepository.findBySlug(slug);
    if (touristicSpot == null) {
      throw new ResourceNotFoundException(slug);
    }

    Comment comment = commentRepository.findByExternalId(id);
    if (comment == null) {
      throw new ResourceNotFoundException(slug);
    }

    UserDetails commentAuthor = comment.getAuthor();
    if (!commentAuthor.getUsername().equals(authenticatedUser.getUsername())) {
      throw new OperationNotAllowedException();
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
