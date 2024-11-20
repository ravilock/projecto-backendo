package com.example.mapaCife.controller;

import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.mapaCife.dto.CreateTouristicSpotDTO;
import com.example.mapaCife.dto.TouristicSpotDTO;
import com.example.mapaCife.dto.TouristicSpotMapper;
import com.example.mapaCife.models.TouristicSpot;
import com.example.mapaCife.repository.TouristicSpotRepository;

import jakarta.validation.Valid;

@RestController
@RequestMapping("api")
@Validated
public class TouristicSpotController {

    @Autowired
    private TouristicSpotRepository touristicSpotRepository;

    @PostMapping("/touristic-spots")
    public ResponseEntity<?> createTouristicSpot(@RequestBody @Valid CreateTouristicSpotDTO dto) {
        String slug = dto.name().replace(" ", "-").toLowerCase();
        if (touristicSpotRepository.findBySlug(slug) != null) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body("touristic spot already exists");
        }
        TouristicSpot touristicSpot = new TouristicSpot();
        touristicSpot.setSlug(slug);
        touristicSpot.setName(dto.name());
        touristicSpot.setDescription(dto.description());
        touristicSpot.setGmapsLink(dto.gmapsLink());
        touristicSpot.setTypeList(dto.typeList());
        touristicSpot.setCreatedAt(new Date());
        touristicSpot.setUpdatedAt(new Date());

        try {
            touristicSpot = touristicSpotRepository.save(touristicSpot);
        } catch (Exception e) {
            System.out.printf("Failed to save touristic spot: %s", e.toString());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("internal server error");
        }

        TouristicSpotDTO response = TouristicSpotMapper.toDTO(touristicSpot);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);

    }
}