package com.example.mapaCife.dto;

import com.example.mapaCife.models.Author;

import lombok.Data;

@Data
public class RatingDTO {
    private int notice;
    private String author;
    private Long touristicSpotId;
    public Object getNotice() {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'getNotice'");
    }
    public Author getAuthor() {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'getAuthor'");
    }
}
