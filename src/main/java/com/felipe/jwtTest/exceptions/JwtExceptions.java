package com.felipe.jwtTest.exceptions;

import org.springframework.http.HttpStatus;

public class JwtExceptions {
  public static class InvalidTokenTypeException extends ApiErrorException {
    public InvalidTokenTypeException(String requiredTokenType) {
      super(String.format("Please use the %s token", requiredTokenType), HttpStatus.BAD_REQUEST);
    }
  }
}
