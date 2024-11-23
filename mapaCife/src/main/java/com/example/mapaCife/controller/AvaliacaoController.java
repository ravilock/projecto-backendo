package com.example.mapaCife.controller;

import com.example.mapaCife.dto.AvaliacaoDTO;
import com.example.mapaCife.service.AvaliacaoService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/avaliacoes")
public class AvaliacaoController {
    private final AvaliacaoService avaliacaoService;

    public AvaliacaoController(AvaliacaoService avaliacaoService) {
        this.avaliacaoService = avaliacaoService;
    }

    @PostMapping
    public ResponseEntity<?> criarAvaliacao(@RequestBody AvaliacaoDTO dto) {
        return ResponseEntity.ok(avaliacaoService.criarAvaliacao(dto));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deletarAvaliacao(@PathVariable Long id) {
        avaliacaoService.deletarAvaliacao(id);
        return ResponseEntity.noContent().build();
    }
}

