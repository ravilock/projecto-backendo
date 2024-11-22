package com.example.mapaCife.exception;

public class ResourceNotFoundException extends RuntimeException {

  public ResourceNotFoundException(String resourceIdentifier) {
    super(String.format("Resource %s was not found", resourceIdentifier));
  }
}
