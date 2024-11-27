package com.example.mapaCife.controller;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.contains;

import java.util.Date;

import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import com.example.mapaCife.config.SecurityConfiguration;
import com.example.mapaCife.dto.AuthenticationDTO;
import com.example.mapaCife.models.User;
import com.example.mapaCife.models.UserRole;
import com.example.mapaCife.repository.UserRepository;
import com.example.mapaCife.service.TokenService;
import com.fasterxml.jackson.databind.ObjectMapper;

@RunWith(SpringRunner.class)
@WebMvcTest(AuthenticationController.class)
@Import(SecurityConfiguration.class)
public class AuthenticationControllerLoginTest {

  @Autowired
  private MockMvc mockMvc;

  @MockBean
  private UserRepository userRepository;

  @MockBean
  private TokenService tokenService;

  @Autowired
  private ObjectMapper objectMapper;

  @Test
  public void testLogin_Success() throws Exception {
    // Arrange
    AuthenticationDTO authenticationDTO = new AuthenticationDTO("test-username", "TestingPassword1!");

    User mockUser = new User();
    mockUser.setId(Long.valueOf(1));
    mockUser.setName("Test User Name");
    mockUser.setEmail("test@test.test");
    mockUser.setPassword(new BCryptPasswordEncoder().encode(authenticationDTO.password()));
    mockUser.setCreatedAt(new Date());
    mockUser.setUsername(authenticationDTO.username());
    mockUser.setRole(UserRole.USER);

    Mockito.when(userRepository.findByUsername(authenticationDTO.username())).thenReturn(mockUser);
    Mockito.when(tokenService.generateToken(mockUser)).thenReturn("token");

    // Act
    ResultActions result = mockMvc.perform(post("/api/users/login")
        .contentType(MediaType.APPLICATION_JSON)
        .content(objectMapper.writeValueAsString(authenticationDTO)));

    // Assert
    result
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.name", is(mockUser.getName())))
        .andExpect(jsonPath("$.email", is(mockUser.getEmail())))
        .andExpect(jsonPath("$.token", is("token")))
        .andExpect(jsonPath("$.username", is(authenticationDTO.username())));
  }

  @Test
  public void testLogin_FailedToFindUser() throws Exception {
    // Arrange
    AuthenticationDTO authenticationDTO = new AuthenticationDTO("test-username", "TestingPassword1!");

    User mockUser = new User();
    mockUser.setId(Long.valueOf(1));
    mockUser.setName("Test User Name");
    mockUser.setEmail("test@test.test");
    mockUser.setPassword(new BCryptPasswordEncoder().encode(authenticationDTO.password()));
    mockUser.setCreatedAt(new Date());
    mockUser.setUsername(authenticationDTO.username());
    mockUser.setRole(UserRole.USER);

    Mockito.when(userRepository.findByUsername(authenticationDTO.username())).thenReturn(null);

    // Act
    ResultActions result = mockMvc.perform(post("/api/users/login")
        .contentType(MediaType.APPLICATION_JSON)
        .content(objectMapper.writeValueAsString(authenticationDTO)));

    // Assert
    result
        .andExpect(status().isUnauthorized())
        .andExpect(jsonPath("$.code", is(HttpStatus.UNAUTHORIZED.value())))
        .andExpect(jsonPath("$.status", is(HttpStatus.UNAUTHORIZED.name())))
        .andExpect(jsonPath("$.errors", contains("Invalid credentials")));
  }

  @Test
  public void testLogin_UnmatchedPasswords() throws Exception {
    // Arrange
    AuthenticationDTO authenticationDTO = new AuthenticationDTO("test-username", "TestingPassword1!");

    User mockUser = new User();
    mockUser.setId(Long.valueOf(1));
    mockUser.setName("Test User Name");
    mockUser.setEmail("test@test.test");
    mockUser.setPassword(new BCryptPasswordEncoder().encode("different-password"));
    mockUser.setCreatedAt(new Date());
    mockUser.setUsername(authenticationDTO.username());
    mockUser.setRole(UserRole.USER);

    Mockito.when(userRepository.findByUsername(authenticationDTO.username())).thenReturn(mockUser);

    // Act
    ResultActions result = mockMvc.perform(post("/api/users/login")
        .contentType(MediaType.APPLICATION_JSON)
        .content(objectMapper.writeValueAsString(authenticationDTO)));

    // Assert
    result
        .andExpect(status().isUnauthorized())
        .andExpect(jsonPath("$.code", is(HttpStatus.UNAUTHORIZED.value())))
        .andExpect(jsonPath("$.status", is(HttpStatus.UNAUTHORIZED.name())))
        .andExpect(jsonPath("$.errors", contains("Invalid credentials")));
  }
}
