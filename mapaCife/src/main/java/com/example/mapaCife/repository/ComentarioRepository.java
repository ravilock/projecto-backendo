package com.example.mapaCife.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.mapaCife.models.Comentario;

@Repository
public interface ComentarioRepository extends JpaRepository<Comentario, Long> {
}

