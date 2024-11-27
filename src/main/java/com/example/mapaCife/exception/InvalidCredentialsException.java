package com.example.mapaCife.exception;

public class InvalidCredentialsException extends RuntimeException {
  public InvalidCredentialsException() {
    super("Invalid credentials");
  }
}
