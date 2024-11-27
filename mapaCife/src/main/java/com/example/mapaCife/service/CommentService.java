package com.example.mapaCife.service;

import java.util.Date;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import com.example.mapaCife.dto.CreateCommentDTO;
import com.example.mapaCife.models.Comment;
import com.example.mapaCife.models.TouristicSpot;
import com.example.mapaCife.models.User;
import com.example.mapaCife.repository.CommentRepository;

@Service
public class CommentService {
  @Autowired
  private CommentRepository commentRepository;

  public Comment createComment(CreateCommentDTO dto, User commentAuthor, TouristicSpot touristicSpot) {
    Comment comment = new Comment();
    comment.setBody(dto.body());
    comment.setExternalId(UUID.randomUUID());
    comment.setCreatedAt(new Date());
    comment.setAuthor(commentAuthor);
    comment.setTouristicSpot(touristicSpot);
    return commentRepository.save(comment);
  }

  public Page<Comment> getCommentsByTouristicSpot(TouristicSpot touristicSpot, int page, int size) {
    PageRequest pageable = PageRequest.of(page, size);
    return commentRepository.findByTouristicSpot(touristicSpot, pageable);
  }
}
