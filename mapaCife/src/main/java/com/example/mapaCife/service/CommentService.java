package com.example.mapaCife.service;

import org.springframework.stereotype.Service;

import com.example.mapaCife.dto.CommentDTO;
import com.example.mapaCife.models.Comment;
import com.example.mapaCife.repository.CommentRepository;

@Service
public class CommentService {
    private static final CommentRepository CommentRepository = null;
        private final CommentRepository commentRepository;
    
        public CommentService(CommentRepository commenRepository) {
            this.commentRepository = CommentRepository;
    }

    public Comment criarComment(CommentDTO dto) {
        Comment comment = new Comment();
        comment.setText(dto.getText());
        comment.setUser(dto.getAuthor());
        // Assuma que o PontoTuristico já foi buscado aqui
        return commentRepository.save(comment);
    }

    public void deletarComment(Long id) {
        commentRepository.deleteById(id);
    }
}
