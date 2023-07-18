package com.felipe.jwtTest.controllers;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.felipe.jwtTest.dtos.requests.LoginRequest;
import com.felipe.jwtTest.dtos.responses.LoginResponse;
import com.felipe.jwtTest.entities.User;
import com.felipe.jwtTest.services.AuthenticationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/auth")
public class AuthController {
  @Autowired private AuthenticationService authenticationService;

  @PostMapping("/login")
  public ResponseEntity<LoginResponse> login(@RequestBody LoginRequest loginRequest)
      throws JsonProcessingException {
    Authentication authentication = authenticationService.authenticate(loginRequest);

    User loggedInUser = (User) authentication.getPrincipal();

    String accessToken = authenticationService.createAccessToken(loggedInUser);
    String refreshToken = authenticationService.createRefreshToken(loggedInUser);

    LoginResponse loginResponse = new LoginResponse(accessToken, refreshToken);

    return new ResponseEntity<>(loginResponse, HttpStatus.OK);
  }
}
