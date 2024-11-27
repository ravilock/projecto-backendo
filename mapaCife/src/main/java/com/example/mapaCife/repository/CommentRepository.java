package com.example.mapaCife.repository;

import java.util.UUID;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.mapaCife.models.Comment;
import com.example.mapaCife.models.TouristicSpot;

@Repository
public interface CommentRepository extends JpaRepository<Comment, Long> {
  Comment findByExternalId(UUID externalId);

  Page<Comment> findByTouristicSpot(TouristicSpot touristicSpot, PageRequest pageRequest);
}
