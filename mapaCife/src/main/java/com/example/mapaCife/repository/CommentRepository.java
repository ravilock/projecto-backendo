package com.example.mapaCife.repository;

import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.mapaCife.models.Comment;

@Repository
public interface CommentRepository extends JpaRepository<Comment, Long> {
  Comment findByExternalId(UUID externalId);
}
