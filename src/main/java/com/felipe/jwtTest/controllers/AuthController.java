package com.felipe.jwtTest.controllers;

import com.felipe.jwtTest.dtos.requests.LoginRequest;
import com.felipe.jwtTest.dtos.responses.LoginResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/auth")
public class AuthController {
  @PostMapping("/login")
  public ResponseEntity<LoginResponse> login(@RequestBody LoginRequest loginRequest) {
    LoginResponse loginResponse = new LoginResponse("accessToken", "refreshToken");
    return new ResponseEntity<LoginResponse>(loginResponse, HttpStatus.OK);
  }
}
