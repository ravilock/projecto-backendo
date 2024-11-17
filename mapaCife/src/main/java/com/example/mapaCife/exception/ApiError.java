package com.example.mapaCife.exception;

import lombok.Data;

@Data
public class ApiError {
  private String code;
  private int status;
  private String message;
}
