package com.example.mapaCife.models;

import java.util.Date;
import java.util.UUID;

import jakarta.persistence.Column;
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

  private String body;

  private Date createdAt;

  @Column(name = "external_id", nullable = false, unique = true, columnDefinition = "BINARY(16)")
  private UUID externalId;

  @ManyToOne
  @JoinColumn(name = "touristic_spot_id", nullable = false)
  private TouristicSpot touristicSpot;

  @ManyToOne
  @JoinColumn(name = "author_id", nullable = false)
  private User author;
}
