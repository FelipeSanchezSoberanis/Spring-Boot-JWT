package com.felipe.jwtTest.exceptions;

import org.springframework.http.HttpStatus;

public class ImprovedBadCredentialsException extends ApiErrorException {
  public ImprovedBadCredentialsException() {
    super("Incorrect credentials", HttpStatus.BAD_REQUEST);
  }
}
