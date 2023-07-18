package com.felipe.jwtTest.exceptionHandlers;

import com.felipe.jwtTest.dtos.responses.ApiErrorResponse;
import com.felipe.jwtTest.exceptions.ApiErrorException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class ApiErrorExceptionHandler {
  @ExceptionHandler(ApiErrorException.class)
  public ResponseEntity<ApiErrorResponse> handleApiErrorException(ApiErrorException ex) {
    ApiErrorResponse res = new ApiErrorResponse();
    res.setMessage(ex.getMessage());
    return new ResponseEntity<>(res, ex.getStatus());
  }
}
