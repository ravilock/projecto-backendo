package com.example.mapaCife.dto;

import com.example.mapaCife.models.User;

public class UserMapper {
  public static UserDTO toDTO(User user, String token) {
    return new UserDTO(user.getName(), user.getEmail(), token, user.getUsername(), user.getCreatedAt());
  }
}
