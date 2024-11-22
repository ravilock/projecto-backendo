package com.example.mapaCife.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.mapaCife.dto.UpdateTouristicSpotDTO;
import com.example.mapaCife.exception.ResourceNotFoundException;
import com.example.mapaCife.models.TouristicSpot;
import com.example.mapaCife.repository.TouristicSpotRepository;

import jakarta.transaction.Transactional;

@Service
public class TouristicSpotService {

  @Autowired
  private TouristicSpotRepository touristicSpotRepository;

  @Transactional
  public TouristicSpot updateTouristicSpot(String slug, UpdateTouristicSpotDTO dto) throws ResourceNotFoundException {
    TouristicSpot touristicSpot = touristicSpotRepository.findBySlug(slug);
    if (touristicSpot == null) {
      throw new ResourceNotFoundException(slug);
    }

    if (dto.name() != null) {
      String newSlug = dto.name().replace(" ", "-").toLowerCase();
      touristicSpot.setSlug(newSlug);
      touristicSpot.setName(dto.name());
    }
    if (dto.gmapsLink() != null) {
      touristicSpot.setGmapsLink(dto.gmapsLink());
    }
    if (dto.description() != null) {
      touristicSpot.setDescription(dto.description());
    }
    if (dto.typeList() != null) {
      touristicSpot.setTypeList(dto.typeList());
    }
    if (dto.paid() != null) {
      touristicSpot.setPaid(dto.paid());
    }

    return touristicSpot;
  }

}
