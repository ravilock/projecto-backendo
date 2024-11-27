package com.example.mapaCife.exception;

public class ResourceAlreadyExistsException extends RuntimeException {

  public ResourceAlreadyExistsException(String resourceIdentifier) {
    super(String.format("Resource %s already exists", resourceIdentifier));
  }
}
