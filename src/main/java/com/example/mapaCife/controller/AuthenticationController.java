package com.example.mapaCife.controller;

import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
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
import com.example.mapaCife.exception.InvalidCredentialsException;
import com.example.mapaCife.exception.ResourceAlreadyExistsException;
import com.example.mapaCife.models.User;
import com.example.mapaCife.models.UserRole;
import com.example.mapaCife.repository.UserRepository;
import com.example.mapaCife.service.TokenService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
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
  @Operation(summary = "Authentication endpoint", method = "POST")
  @ApiResponses(value = {
      @ApiResponse(responseCode = "200", description = "Authetication successfull", content = {
          @Content(mediaType = "application/json", schema = @Schema(implementation = UserDTO.class)) })
  })
  public ResponseEntity<?> login(@RequestBody @Valid AuthenticationDTO dto) {
    UserDetails user = userRepository.findByUsername(dto.username());
    if (user == null) {
      throw new InvalidCredentialsException();
    }
    boolean passwordMatches = new BCryptPasswordEncoder().matches(dto.password(), user.getPassword());
    if (!passwordMatches) {
      throw new InvalidCredentialsException();
    }

    String token = tokenService.generateToken((User) user);
    UserDTO response = UserMapper.toDTO((User) user, token);
    return ResponseEntity.ok(response);
  }

  @PostMapping("/users")
  @Operation(summary = "User registration endpoint", method = "POST")
  @ApiResponses(value = {
      @ApiResponse(responseCode = "201", description = "Registration successfull", content = {
          @Content(mediaType = "application/json", schema = @Schema(implementation = UserDTO.class)) })
  })
  public ResponseEntity<?> register(@RequestBody @Valid RegisterDTO registerDTO) {
    if (userRepository.findByUsernameOrEmail(registerDTO.username(), registerDTO.email()) != null) {
      throw new ResourceAlreadyExistsException(registerDTO.username());
    }

    User user = new User();
    user.setUsername(registerDTO.username());
    user.setPassword(new BCryptPasswordEncoder().encode(registerDTO.password()));
    user.setRole(UserRole.USER);
    user.setName(registerDTO.name());
    user.setEmail(registerDTO.email());
    user.setCreatedAt(new Date());

    user = userRepository.save(user);

    String token = tokenService.generateToken(user);
    UserDTO response = UserMapper.toDTO(user, token);
    return ResponseEntity.status(HttpStatus.CREATED).body(response);

  }
}
