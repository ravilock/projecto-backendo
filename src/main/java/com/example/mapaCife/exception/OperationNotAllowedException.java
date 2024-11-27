package com.example.mapaCife.exception;

public class OperationNotAllowedException extends RuntimeException {
  public OperationNotAllowedException() {
    super("Action was not allowed");
  }
}
