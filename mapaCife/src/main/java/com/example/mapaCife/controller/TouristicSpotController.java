package com.example.mapaCife.controller;

import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.mapaCife.dto.CreateTouristicSpotDTO;
import com.example.mapaCife.dto.TouristicSpotDTO;
import com.example.mapaCife.dto.TouristicSpotMapper;
import com.example.mapaCife.dto.UpdateTouristicSpotDTO;
import com.example.mapaCife.exception.ResourceAlreadyExistsException;
import com.example.mapaCife.exception.ResourceNotFoundException;
import com.example.mapaCife.models.TouristicSpot;
import com.example.mapaCife.repository.TouristicSpotRepository;
import com.example.mapaCife.service.TouristicSpotService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("api")
@Validated
public class TouristicSpotController {

  @Autowired
  private TouristicSpotRepository touristicSpotRepository;

  @Autowired
  private TouristicSpotService touristicSpotService;

  @PostMapping("/touristic-spots")
  public ResponseEntity<?> createTouristicSpot(@RequestBody @Valid CreateTouristicSpotDTO dto) {
    String slug = dto.name().replace(" ", "-").toLowerCase();
    if (touristicSpotRepository.findBySlug(slug) != null) {
      throw new ResourceAlreadyExistsException(slug);
    }
    TouristicSpot touristicSpot = new TouristicSpot();
    touristicSpot.setSlug(slug);
    touristicSpot.setName(dto.name());
    touristicSpot.setDescription(dto.description());
    touristicSpot.setGmapsLink(dto.gmapsLink());
    touristicSpot.setCreatedAt(new Date());
    touristicSpot.setPaid(dto.paid());

    touristicSpot = touristicSpotRepository.save(touristicSpot);

    TouristicSpotDTO response = TouristicSpotMapper.toDTO(touristicSpot);
    return ResponseEntity.status(HttpStatus.CREATED).body(response);
  }

  @GetMapping("touristic-spots")
  public ResponseEntity<?> listTouristicSpots(
      @RequestParam(required = false) String name,
      @RequestParam(defaultValue = "1") int page,
      @RequestParam(defaultValue = "10") int size) {
    Page<TouristicSpot> touristicSpots = touristicSpotService.getTouristicSpotsByName(name, page - 1, size);
    List<TouristicSpotDTO> response = TouristicSpotMapper.toDTOList(touristicSpots.getContent());
    return ResponseEntity.status(HttpStatus.OK).body(response);
  }

  @GetMapping("touristic-spots/{slug}")
  public ResponseEntity<?> getTouristicSpot(@PathVariable String slug) {
    TouristicSpot touristicSpot = touristicSpotRepository.findBySlug(slug);
    if (touristicSpot == null) {
      throw new ResourceNotFoundException(slug);
    }
    TouristicSpotDTO response = TouristicSpotMapper.toDTO(touristicSpot);
    return ResponseEntity.status(HttpStatus.OK).body(response);
  }

  @PutMapping("/touristic-spots/{slug}")
  public ResponseEntity<?> updateTouristicSpot(@PathVariable String slug,
      @RequestBody @Valid UpdateTouristicSpotDTO dto) {
    TouristicSpot touristicSpot = touristicSpotService.updateTouristicSpot(slug, dto);
    TouristicSpotDTO response = TouristicSpotMapper.toDTO(touristicSpot);
    return ResponseEntity.status(HttpStatus.OK).body(response);
  }

  @DeleteMapping("/touristic-spots/{slug}")
  public ResponseEntity<?> deleteTouristicSpot(@PathVariable String slug) {
    TouristicSpot touristicSpot = touristicSpotRepository.findBySlug(slug);
    if (touristicSpot == null) {
      throw new ResourceNotFoundException(slug);
    }
    touristicSpotRepository.delete(touristicSpot);
    return ResponseEntity.status(HttpStatus.OK).build();
  }
}
