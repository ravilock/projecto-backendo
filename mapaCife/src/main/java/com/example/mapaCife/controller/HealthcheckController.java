package com.example.mapaCife.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping()
public class HealthcheckController {
  @GetMapping("/healthcheck")
  public ResponseEntity<String> healthcheck() {
    return ResponseEntity.ok("WORKING");
  }
}
