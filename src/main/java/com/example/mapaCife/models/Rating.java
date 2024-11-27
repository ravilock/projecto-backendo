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
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
import lombok.Data;

@Data
@Entity
@Table(uniqueConstraints = @UniqueConstraint(columnNames = { "author_id", "touristic_spot_id" }))
public class Rating {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  private Double rating;

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
