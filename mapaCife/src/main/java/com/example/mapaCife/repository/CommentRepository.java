package com.example.mapaCife.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.mapaCife.models.Comment;

@Repository
public interface CommentRepository extends JpaRepository<Comment, Long> {
}

