package com.example.mapaCife.controller;

import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.mapaCife.dto.AuthenticationDTO;
import com.example.mapaCife.dto.RegisterDTO;
import com.example.mapaCife.dto.UserDTO;
import com.example.mapaCife.dto.UserMapper;
import com.example.mapaCife.models.User;
import com.example.mapaCife.models.UserRole;
import com.example.mapaCife.repository.UserRepository;
import com.example.mapaCife.service.TokenService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("api")
@Validated
public class AuthenticationController {
  @Autowired
  private UserRepository userRepository;

  @Autowired
  private TokenService tokenService;

  @PostMapping("/users/login")
  public ResponseEntity<?> login(@RequestBody @Valid AuthenticationDTO dto) {
    UserDetails user = userRepository.findByUsername(dto.username());
    if (user == null) {
      return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid credentials");
    }
    boolean passwordMatches = new BCryptPasswordEncoder().matches(dto.password(), user.getPassword());
    if (!passwordMatches) {
      return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid credentials");
    }

    String token = tokenService.generateToken((User) user);
    UserDTO response = UserMapper.toDTO((User) user, token);
    return ResponseEntity.ok(response);
  }

  @PostMapping("/users")
  public ResponseEntity<?> register(@RequestBody @Valid RegisterDTO registerDTO) {
    if (userRepository.findByUsername(registerDTO.username()) != null) {
      return ResponseEntity.status(HttpStatus.CONFLICT).body("user already exists");
    }

    User user = new User();
    user.setUsername(registerDTO.username());
    user.setPassword(new BCryptPasswordEncoder().encode(registerDTO.password()));
    user.setRole(UserRole.USER);
    user.setName(registerDTO.name());
    user.setEmail(registerDTO.email());
    user.setCreatedAt(new Date());

    try {
      userRepository.save(user);
    } catch (Exception e) {
      System.out.printf("Failed to save user: %s", e.toString());
      return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("internal server error");
    }

    UserDTO response = UserMapper.toDTO(user, "");
    return ResponseEntity.status(HttpStatus.CREATED).body(response);

  }
}
