package com.example.mapaCife.dto;

import lombok.Data;

@Data
public class ComentarioDTO {
    private String texto;
    private String usuario;
    private Long pontoTuristicoId;
}
