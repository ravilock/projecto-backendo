package com.example.mapaCife.service;

import org.springframework.stereotype.Service;

import com.example.mapaCife.dto.AvaliacaoDTO;
import com.example.mapaCife.models.Avaliacao;
import com.example.mapaCife.repository.AvaliacaoRepository;

@Service
public class AvaliacaoService {
    private final AvaliacaoRepository avaliacaoRepository;

    public AvaliacaoService(AvaliacaoRepository avaliacaoRepository) {
        this.avaliacaoRepository = avaliacaoRepository;
    }

    public Avaliacao criarAvaliacao(AvaliacaoDTO dto) {
        Avaliacao avaliacao = new Avaliacao();
        avaliacao.setNota(dto.getNota());
        avaliacao.setUsuario(dto.getUsuario());
        return avaliacaoRepository.save(avaliacao);
    }

    public void deletarAvaliacao(Long id) {
        avaliacaoRepository.deleteById(id);
    }
}

