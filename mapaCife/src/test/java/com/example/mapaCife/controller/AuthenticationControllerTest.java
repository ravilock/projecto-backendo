package com.example.mapaCife.controller;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.hamcrest.Matchers.is;

import java.util.Date;

import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import com.example.mapaCife.config.SecurityConfiguration;
import com.example.mapaCife.dto.RegisterDTO;
import com.example.mapaCife.models.User;
import com.example.mapaCife.models.UserRole;
import com.example.mapaCife.repository.UserRepository;
import com.example.mapaCife.service.TokenService;
import com.fasterxml.jackson.databind.ObjectMapper;

@RunWith(SpringRunner.class)
@WebMvcTest(AuthenticationController.class)
@Import(SecurityConfiguration.class)
public class AuthenticationControllerTest {

  @Autowired
  private MockMvc mockMvc;

  @MockBean
  private UserRepository userRepository;

  @MockBean
  private TokenService tokenService;

  @MockBean
  private AuthenticationManager authenticationManager;

  @Autowired
  private ObjectMapper objectMapper;

  @Test
  public void testRegister_Success() throws Exception {
    // Arrange
    RegisterDTO registerDTO = new RegisterDTO("test-username", "TestingPassword1!", "Test User Name", "test@test.test");

    User mockUser = new User();
    mockUser.setId(Long.valueOf(1));
    mockUser.setName(registerDTO.name());
    mockUser.setEmail(registerDTO.email());
    mockUser.setPassword(registerDTO.password());
    mockUser.setCreatedAt(new Date());
    mockUser.setUsername(registerDTO.username());
    mockUser.setRole(UserRole.USER);

    Mockito.when(userRepository.findByUsername(registerDTO.username())).thenReturn(null);
    Mockito.when(userRepository.save(Mockito.any(User.class))).thenReturn(mockUser);

    // Act
    ResultActions result = mockMvc.perform(post("/api/users")
        .contentType(MediaType.APPLICATION_JSON)
        .content(objectMapper.writeValueAsString(registerDTO)));

    // Assert
    result
        .andExpect(status().isCreated())
        .andExpect(jsonPath("$.name", is(registerDTO.name())))
        .andExpect(jsonPath("$.email", is(registerDTO.email())))
        .andExpect(jsonPath("$.token", is("")))
        .andExpect(jsonPath("$.username", is(registerDTO.username())));
  }

  @Test
  public void testRegister_UserAlreadyExists() throws Exception {
    // Arrange
    RegisterDTO registerDTO = new RegisterDTO("test-username", "TestingPassword1!", "Test User Name", "test@test.test");

    User mockUser = new User();
    mockUser.setId(Long.valueOf(1));
    mockUser.setName(registerDTO.name());
    mockUser.setEmail(registerDTO.email());
    mockUser.setPassword(registerDTO.password());
    mockUser.setCreatedAt(new Date());
    mockUser.setUsername(registerDTO.username());
    mockUser.setRole(UserRole.USER);

    Mockito.when(userRepository.findByUsername(registerDTO.username())).thenReturn(mockUser);

    // Act
    ResultActions result = mockMvc.perform(post("/api/users")
        .contentType(MediaType.APPLICATION_JSON)
        .content(objectMapper.writeValueAsString(registerDTO)));

    // Assert
    result
        .andExpect(status().isConflict())
        .andExpect(content().string("user already exists"));
  }

  @Test
  public void testRegister_FailedToSave() throws Exception {
    // Arrange
    RegisterDTO registerDTO = new RegisterDTO("test-username", "TestingPassword1!", "Test User Name", "test@test.test");

    Mockito.when(userRepository.findByUsername(registerDTO.username())).thenReturn(null);
    Mockito.when(userRepository.save(Mockito.any(User.class))).thenThrow(new RuntimeException("failed to save user"));

    // Act
    ResultActions result = mockMvc.perform(post("/api/users")
        .contentType(MediaType.APPLICATION_JSON)
        .content(objectMapper.writeValueAsString(registerDTO)));

    // Assert
    result
        .andExpect(status().isInternalServerError())
        .andExpect(content().string("internal server error"));
  }

  @Override
  public String toString() {
    return "AuthenticationControllerTest [mockMvc=" + mockMvc + "]";
  }
}
