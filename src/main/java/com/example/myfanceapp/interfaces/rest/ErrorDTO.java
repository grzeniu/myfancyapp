package com.example.myfanceapp.interfaces.rest;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.FieldError;

@Getter
@RequiredArgsConstructor
class ErrorDTO<T> {

  private final List<T> errors;

  static ErrorDTO<Error> create(String description) {
    return new ErrorDTO<>(Collections.singletonList(new Error(description)));
  }

  static ErrorDTO<ErrorField> create(List<FieldError> fieldErrors) {
    List<ErrorField> errorFields =
        fieldErrors.stream()
            .map(error -> new ErrorField(error.getField(), error.getDefaultMessage()))
            .collect(Collectors.toList());
    return new ErrorDTO<>(errorFields);
  }

  @Getter
  @RequiredArgsConstructor
  static class Error {
    private final String description;
  }

  @Getter
  @RequiredArgsConstructor
  static class ErrorField {
    private final String field;
    private final String descriptions;
  }
}
