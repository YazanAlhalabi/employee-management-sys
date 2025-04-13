package com.myFullstackYazan.employee_management.shared;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.resource.NoResourceFoundException;

import java.util.List;

@ControllerAdvice
public class GlobalExceptionResponse {

  @ExceptionHandler(NoResourceFoundException.class)
  public ResponseEntity<GlobalResponse<?>> handleNoResourceException(NoResourceFoundException ex) {
    var errors = List.of(new GlobalResponse.ErrorItem("Resource is not found"));
    return new ResponseEntity<>(new GlobalResponse<>(errors), HttpStatus.NOT_FOUND);
  }

  @ExceptionHandler(CustomResponseException.class)
  public ResponseEntity<GlobalResponse<?>> handleCustomResException(CustomResponseException ex) {
    var errors = List.of(new GlobalResponse.ErrorItem(ex.getMessage()));
    return new ResponseEntity<>(new GlobalResponse<>(errors), HttpStatus.resolve(ex.getCode()));
  }

  @ExceptionHandler(MethodArgumentNotValidException.class)
  public ResponseEntity<GlobalResponse<?>> handleValidationException(MethodArgumentNotValidException ex) {
    var errors = ex.getBindingResult().getFieldErrors().stream()
        .map(err -> new GlobalResponse.ErrorItem(err.getField() + " " + err.getDefaultMessage()))
        .toList();

    return new ResponseEntity<>(new GlobalResponse<>(errors), HttpStatus.BAD_REQUEST);
  }
}
