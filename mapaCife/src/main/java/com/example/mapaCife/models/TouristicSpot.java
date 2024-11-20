package com.example.mapaCife.models;

import java.util.Date;
import java.util.List;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Data;

@Entity
@Table(name = "TouristicSpot")
@Data
public class TouristicSpot {
  @Id
  @GeneratedValue(strategy = GenerationType.SEQUENCE)
  private Long id;

  private String slug;
  private String name;
  private String description;
  private String gmapsLink;
  private List<String> typeList;
  private Date createdAt;
  private Date updatedAt;
  private Boolean paid;
  private Float averagRating;

}
