package com.example.mapaCife.controller;

import com.example.mapaCife.dto.CommentDTO;
import com.example.mapaCife.models.TouristicSpot;
import com.example.mapaCife.repository.TouristicSpotRepository;
import com.example.mapaCife.service.Autowired;
import com.example.mapaCife.service.CommentService;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/comment")
public class CommentController {
    private final CommentService commentService;

    public CommentController(CommentService commentService) {
        this.commentService = commentService;
    }

    @Autowired
    private TouristicSpotRepository touristicSpotRepository;

    @PostMapping("/tourist-spots/{slug}/comment")
    public ResponseEntity<?> criarComment(@PathVariable String slug, @RequestBody CommentDTO dto) {
        TouristicSpot touristicSpot = touristicSpotRepository.findBySlug(slug);
        if (touristicSpot == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Touristic Spot not found!");
        }
        return ResponseEntity.ok(commentService.criarComment(dto));
    }

    @DeleteMapping("/tourist-spots/{slug}/ratings/{id}")
    public ResponseEntity<?> deletarComment(@PathVariable String slug, @PathVariable Long id) {
        commentService.deletarComment(id);
        TouristicSpot touristicSpot = touristicSpotRepository.findBySlug(slug);
        if (touristicSpot == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Touristic Spot not found!");
        }
        commentService.deletarComment(id);

        return ResponseEntity.noContent().build();
    }
}
