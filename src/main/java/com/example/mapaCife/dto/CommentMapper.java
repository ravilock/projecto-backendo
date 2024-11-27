package com.example.mapaCife.dto;

import java.util.List;

import com.example.mapaCife.models.Comment;

public class CommentMapper {
  public static CommentDTO toDTO(Comment comment) {
    return new CommentDTO(
        comment.getExternalId(),
        comment.getCreatedAt(),
        comment.getBody(),
        comment.getAuthor().getUsername());
  }

  public static List<CommentDTO> toDTOList(List<Comment> comments) {
    return comments.stream().map(CommentMapper::toDTO).toList();
  }
}
