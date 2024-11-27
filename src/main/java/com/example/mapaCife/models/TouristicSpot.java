package com.example.mapaCife.models;

import java.util.Date;
import java.util.List;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import lombok.Data;

@Entity
@Table(name = "TouristicSpot")
@Data
public class TouristicSpot {
  @Id
  @GeneratedValue(strategy = GenerationType.SEQUENCE)
  private Long id;

  @Column(unique = true)
  private String slug;

  private String name;

  private String description;

  private String gmapsLink;

  private Date createdAt;

  private Date updatedAt;

  private Boolean paid;

  private Float averageRating;

  @OneToMany(mappedBy = "touristicSpot", cascade = CascadeType.ALL, orphanRemoval = true)
  private List<Comment> comments;

  @OneToMany(mappedBy = "touristicSpot", cascade = CascadeType.ALL, orphanRemoval = true)
  private List<Rating> ratings;
}
