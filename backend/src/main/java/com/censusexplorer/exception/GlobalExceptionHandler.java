package com.censusexplorer.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

/**
 * Global exception handler that maps exceptions thrown by controllers and services
 * to appropriate HTTP response status codes.
 *
 * <p>This keeps all exception-to-status mapping in one place and ensures that
 * controllers remain free of error-handling logic.
 */
@RestControllerAdvice
public class GlobalExceptionHandler {

  /**
   * Handles {@link IllegalArgumentException} by returning a 400 Bad Request response.
   *
   * <p>This is thrown by the service layer when request parameters fail validation
   * (e.g. an invalid or unsupported census year).
   *
   * @param exception the exception that was thrown
   * @return a 400 response with no body
   */
  @ExceptionHandler(IllegalArgumentException.class)
  public ResponseEntity<Void> handleIllegalArgumentException(
      IllegalArgumentException exception) {
    return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
  }

  /**
   * Handles all other uncaught exceptions by returning a 500 Internal Server Error
   * response.
   *
   * <p>Raw error details are intentionally omitted from the response body to avoid
   * leaking internal information to clients.
   *
   * @param exception the exception that was thrown
   * @return a 500 response with no body
   */
  @ExceptionHandler(Exception.class)
  public ResponseEntity<Void> handleException(Exception exception) {
    return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
  }
}
