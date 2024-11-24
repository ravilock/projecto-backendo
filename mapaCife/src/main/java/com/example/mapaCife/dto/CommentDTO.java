package com.example.mapaCife.dto;

import lombok.Data;

@Data
public class CommentDTO {
    private String text;
    private String author;
    private Long touristicSpotId;
}
