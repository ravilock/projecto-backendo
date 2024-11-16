package com.example.mapaCife.dto;

import com.example.mapaCife.models.UserRole;

public record RegisterDTO(String username, String password, UserRole role, String name, String email) {

}
