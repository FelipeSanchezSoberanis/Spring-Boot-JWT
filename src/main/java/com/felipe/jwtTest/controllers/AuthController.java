package com.felipe.jwtTest.controllers;

import com.felipe.jwtTest.dtos.requests.LoginRequest;
import com.felipe.jwtTest.dtos.responses.LoginResponse;
import com.felipe.jwtTest.exceptions.ImprovedBadCredentialsException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/auth")
public class AuthController {
  @Autowired private AuthenticationManager authenticationManager;

  @PostMapping("/login")
  public ResponseEntity<LoginResponse> login(@RequestBody LoginRequest loginRequest) {
    try {
      Authentication authentication =
          authenticationManager.authenticate(
              new UsernamePasswordAuthenticationToken(
                  loginRequest.getUsername(), loginRequest.getPassword()));

    } catch (BadCredentialsException ex) {
      throw new ImprovedBadCredentialsException();
    }
    return new ResponseEntity<>(HttpStatus.OK);
  }
}
