package com.example.mapaCife.models;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.Data;

@Data
@Entity
public class Comment {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String text;

    private String User;

    @ManyToOne
    @JoinColumn(name = "touristic_spot_id", nullable = false)
    private TouristicSpot touristicSpot;

    @JoinColumn(name = "author_id", nullable = false)
    private Author author;
}