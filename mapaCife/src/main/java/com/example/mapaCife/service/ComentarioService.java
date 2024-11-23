package com.example.mapaCife.service;

import org.springframework.stereotype.Service;

import com.example.mapaCife.dto.ComentarioDTO;
import com.example.mapaCife.models.Comentario;
import com.example.mapaCife.repository.ComentarioRepository;

@Service
public class ComentarioService {
    private final ComentarioRepository comentarioRepository;

    public ComentarioService(ComentarioRepository comentarioRepository) {
        this.comentarioRepository = comentarioRepository;
    }

    public Comentario criarComentario(ComentarioDTO dto) {
        Comentario comentario = new Comentario();
        comentario.setTexto(dto.getTexto());
        comentario.setUsuario(dto.getUsuario());
        // Assuma que o PontoTuristico já foi buscado aqui
        return comentarioRepository.save(comentario);
    }

    public void deletarComentario(Long id) {
        comentarioRepository.deleteById(id);
    }
}
