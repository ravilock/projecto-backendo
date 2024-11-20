package com.example.mapaCife.dto;

import com.example.mapaCife.models.TouristicSpot;

public class TouristicSpotMapper {
    public static TouristicSpotDTO toDTO(TouristicSpot touristicSpot) {
        return new TouristicSpotDTO(touristicSpot.getSlug(), touristicSpot.getName(), touristicSpot.getDescription(), touristicSpot.getGmapsLink(), touristicSpot.getTypeList(), touristicSpot.getCreatedAt(), touristicSpot.getUpdatedAt(), touristicSpot.getAveragRating());    }    
}
