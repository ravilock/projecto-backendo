package com.example.mapaCife.dto;

import com.example.mapaCife.models.Comment;

public class CommentMapper {
  public static CommentDTO toDto(Comment comment) {
    return new CommentDTO(
        comment.getExternalId(),
        comment.getCreatedAt(),
        comment.getBody(),
        comment.getAuthor().getUsername());
  }
}
