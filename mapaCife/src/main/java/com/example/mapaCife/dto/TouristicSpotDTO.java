package com.example.mapaCife.dto;

import java.util.Date;

public record TouristicSpotDTO(String slug, String name, String description, String gmapsLink,
    Date createdAt, Date updateAt, Float averagRating, Boolean paid) {
}
