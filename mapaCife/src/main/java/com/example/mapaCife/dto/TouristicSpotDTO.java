package com.example.mapaCife.dto;

import java.util.Date;
import java.util.List;

public record TouristicSpotDTO(String slug, String name, String description, String gmapsLink, List<String> typeList,
        Date createdAt, Date updateAt, Float averagRating) {
}
