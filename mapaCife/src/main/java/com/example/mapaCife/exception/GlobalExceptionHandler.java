package com.example.mapaCife.exception;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {
  private final Logger logger = LoggerFactory.getLogger(this.getClass());

  @ExceptionHandler(Exception.class)
  public ResponseEntity<ApiError> genericException(Exception ex) {
    logger.error(ex.getMessage());
    ApiError apiError = ApiError
        .builder()
        .code(HttpStatus.INTERNAL_SERVER_ERROR.value())
        .status(HttpStatus.INTERNAL_SERVER_ERROR.name())
        .errors(List.of("Internal Server Error"))
        .build();
    return new ResponseEntity<>(apiError, HttpStatus.INTERNAL_SERVER_ERROR);
  }

  @ExceptionHandler(InvalidCredentialsException.class)
  public ResponseEntity<ApiError> handleAuthenticationExpcetions(RuntimeException ex) {
    logger.debug(ex.getMessage());
    ApiError apiError = ApiError
        .builder()
        .code(HttpStatus.UNAUTHORIZED.value())
        .status(HttpStatus.UNAUTHORIZED.name())
        .errors(List.of(ex.getMessage()))
        .build();
    return new ResponseEntity<>(apiError, HttpStatus.UNAUTHORIZED);
  }

  @ExceptionHandler(MethodArgumentNotValidException.class)
  public ResponseEntity<ApiError> handleValidationExceptions(MethodArgumentNotValidException ex) {
    logger.debug(String.format("Validation error: %s", ex.getMessage()));
    List<String> errorList = ex.getBindingResult()
        .getFieldErrors()
        .stream()
        .map(error -> error.getField() + ": " + error.getDefaultMessage())
        .toList();
    ApiError apiError = ApiError
        .builder()
        .code(HttpStatus.UNPROCESSABLE_ENTITY.value())
        .status(HttpStatus.UNPROCESSABLE_ENTITY.name())
        .errors(errorList)
        .build();
    return new ResponseEntity<>(apiError, HttpStatus.UNPROCESSABLE_ENTITY);
  }

  @ExceptionHandler(ResourceNotFoundException.class)
  public ResponseEntity<ApiError> handleResourceNotFoundException(ResourceNotFoundException ex) {
    logger.debug(ex.getMessage());
    ApiError apiError = ApiError
        .builder()
        .code(HttpStatus.NOT_FOUND.value())
        .status(HttpStatus.NOT_FOUND.name())
        .errors(List.of(ex.getMessage()))
        .build();
    return new ResponseEntity<>(apiError, HttpStatus.NOT_FOUND);
  }

  @ExceptionHandler(ResourceAlreadyExistsException.class)
  public ResponseEntity<ApiError> handleResourceAlreadyExistsException(ResourceAlreadyExistsException ex) {
    logger.debug(ex.getMessage());
    ApiError apiError = ApiError
        .builder()
        .code(HttpStatus.CONFLICT.value())
        .status(HttpStatus.CONFLICT.name())
        .errors(List.of(ex.getMessage()))
        .build();
    return new ResponseEntity<>(apiError, HttpStatus.CONFLICT);
  }

  @ExceptionHandler(OperationNotAllowedException.class)
  public ResponseEntity<ApiError> handleOperationNotAllowedException(OperationNotAllowedException ex) {
    ApiError apiError = ApiError
        .builder()
        .code(HttpStatus.FORBIDDEN.value())
        .status(HttpStatus.FORBIDDEN.name())
        .errors(List.of(ex.getMessage()))
        .build();
    return new ResponseEntity<>(apiError, HttpStatus.FORBIDDEN);
  }

}
