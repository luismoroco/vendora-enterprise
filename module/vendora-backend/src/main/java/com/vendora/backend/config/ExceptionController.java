package com.vendora.backend.config;

import com.vendora.backend.common.exc.BadRequestException;
import com.vendora.backend.common.exc.NotFoundException;
import com.vendora.backend.common.web.api.ApiError;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.HashMap;
import java.util.Map;

@RestControllerAdvice
public class ExceptionController {
  @ExceptionHandler({NotFoundException.class})
  public ResponseEntity<ApiError> notFoundException(NotFoundException exc) {
    return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ApiError(exc));
  }

  @ExceptionHandler({BadRequestException.class})
  public ResponseEntity<ApiError> badRequestException(BadRequestException exc) {
    return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ApiError(exc));
  }

  @ExceptionHandler({MethodArgumentNotValidException.class})
  public ResponseEntity<Map<String, String>> handleValidationExceptions(MethodArgumentNotValidException exc) {
    HashMap<String, String> errors = new HashMap<>();
    exc.getBindingResult().getAllErrors().forEach(error -> {
      String fieldName = ((FieldError) error).getField();
      String errorMessage = error.getDefaultMessage();
      errors.put(fieldName, errorMessage);
    });

    return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errors);
  }
}
