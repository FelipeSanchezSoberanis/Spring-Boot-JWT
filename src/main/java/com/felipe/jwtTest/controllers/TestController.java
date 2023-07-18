package com.felipe.jwtTest.controllers;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/test")
public class TestController {
  @GetMapping("/public")
  private ResponseEntity<String> publicEndpoint() {
    return new ResponseEntity<>("Hi all!", HttpStatus.OK);
  }

  @GetMapping("/private")
  private ResponseEntity<String> privateEndpoint() {
    return new ResponseEntity<>("Hi user!", HttpStatus.OK);
  }
}
