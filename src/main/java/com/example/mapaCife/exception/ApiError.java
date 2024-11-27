package com.example.mapaCife.exception;

import java.util.List;

import lombok.Builder;

@Builder
public record ApiError(
    Integer code,
    String status,
    List<String> errors) {
}
