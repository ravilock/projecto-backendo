package com.example.mapaCife.models;

import lombok.Getter;

@Getter
public enum UserRole {
  ADMIN("admin"),
  USER("user");

  private String role;

  UserRole(String role) {
    this.role = role;
  }
}
