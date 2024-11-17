package com.example.mapaCife.dto;

import jakarta.validation.constraints.*;

public record RegisterDTO(
    @NotNull @NotBlank @Size(min = 4, max = 255) @Pattern(regexp = "\\S+", message = "Username must not contain any spaces") String username,
    @NotNull @NotBlank @Size(min = 6, max = 255) @Pattern(regexp = "^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[!@#$%^&*()_+=\\[\\]{}|;:'\",.<>?]).+$", message = "Password must contain at least one number, one uppercase letter, one lowercase letter, and one special character") String password,
    @NotNull @NotBlank @Size(min = 4, max = 255) String name,
    @NotNull @NotBlank @Email(message = "Email format is invalid") String email) {

}
