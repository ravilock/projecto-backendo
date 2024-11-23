package com.example.mapaCife.controller;

import com.example.mapaCife.dto.ComentarioDTO;
import com.example.mapaCife.service.ComentarioService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/comentarios")
public class ComentarioController {
    private final ComentarioService comentarioService;

    public ComentarioController(ComentarioService comentarioService) {
        this.comentarioService = comentarioService;
    }

    @PostMapping
    public ResponseEntity<?> criarComentario(@RequestBody ComentarioDTO dto) {
        return ResponseEntity.ok(comentarioService.criarComentario(dto));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deletarComentario(@PathVariable Long id) {
        comentarioService.deletarComentario(id);
        return ResponseEntity.noContent().build();
    }
}
