package com.example.myfanceapp.interfaces.rest;

import com.example.myfanceapp.product.ProductNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

@ControllerAdvice
public class RestExceptionHandler {

  // TODO Due to security reasons we can return 403 (Forbidden) for ProviderNotFoundException
  @ResponseBody
  @ResponseStatus(value = HttpStatus.NOT_FOUND)
  @ExceptionHandler(ProductNotFoundException.class)
  public ResponseEntity<ErrorDTO<ErrorDTO.Error>> handle(ProductNotFoundException ex) {
    ErrorDTO<ErrorDTO.Error> error = ErrorDTO.create(ex.getMessage());
    return new ResponseEntity<>(error, HttpStatus.NOT_FOUND);
  }

  @ResponseBody
  @ResponseStatus(HttpStatus.BAD_REQUEST)
  @ExceptionHandler(MethodArgumentNotValidException.class)
  protected ResponseEntity<ErrorDTO<ErrorDTO.ErrorField>> handle(
      MethodArgumentNotValidException ex) {
    ErrorDTO<ErrorDTO.ErrorField> error = ErrorDTO.create(ex.getFieldErrors());
    return new ResponseEntity<>(error, HttpStatus.BAD_REQUEST);
  }

  @ResponseBody
  @ResponseStatus(HttpStatus.UNPROCESSABLE_ENTITY)
  @ExceptionHandler(IllegalArgumentException.class)
  protected ResponseEntity<ErrorDTO<ErrorDTO.Error>> handle(IllegalArgumentException ex) {
    ErrorDTO<ErrorDTO.Error> error = ErrorDTO.create(ex.getMessage());
    return new ResponseEntity<>(error, HttpStatus.UNPROCESSABLE_ENTITY);
  }

  @ResponseBody
  @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
  @ExceptionHandler(Exception.class)
  public ResponseEntity<ErrorDTO<ErrorDTO.Error>> handle(Exception ex) {
    ErrorDTO<ErrorDTO.Error> error = ErrorDTO.create(ex.getMessage());
    return new ResponseEntity<>(error, HttpStatus.INTERNAL_SERVER_ERROR);
  }
}
