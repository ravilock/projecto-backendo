package com.example.mapaCife.dto;

import java.util.Date;

public record UserDTO(String name, String email, String token, String username, Date createdAt) {
}
