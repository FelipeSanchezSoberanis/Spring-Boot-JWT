package com.felipe.jwtTest;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import com.felipe.jwtTest.dtos.requests.LoginRequest;
import com.felipe.jwtTest.dtos.responses.LoginResponse;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
@ExtendWith(SpringExtension.class)
class JwtTestApplicationTests {
  RestTemplate restTemplate = new RestTemplate();

  private String appUrl(String relativeUrl) {
    return "http://localhost:8080" + relativeUrl;
  }

  @Test
  void testLogin() {
    LoginRequest loginRequest = new LoginRequest("user", "password");
    ResponseEntity<LoginResponse> loginResponse =
        restTemplate.exchange(
            appUrl("/auth/login"),
            HttpMethod.POST,
            new HttpEntity<>(loginRequest),
            LoginResponse.class);

    assertEquals(HttpStatus.OK, loginResponse.getStatusCode());
    assertNotNull(loginResponse.getBody().getAccessToken());
    assertNotNull(loginResponse.getBody().getRefreshToken());
  }

  @Test
  void testAccessingProtectedResource() {
    // Unauthenticated request
    try {
      restTemplate.exchange(appUrl("/test/private"), HttpMethod.GET, null, Void.class);
    } catch (HttpClientErrorException e) {
      assertEquals(HttpStatus.FORBIDDEN, e.getStatusCode());
    }

    // Login
    LoginRequest loginRequest = new LoginRequest("user", "password");
    ResponseEntity<LoginResponse> loginResponse =
        restTemplate.exchange(
            appUrl("/auth/login"),
            HttpMethod.POST,
            new HttpEntity<>(loginRequest),
            LoginResponse.class);

    // Authenticated request
    HttpHeaders httpHeaders = new HttpHeaders();
    httpHeaders.add(
        HttpHeaders.AUTHORIZATION, "Bearer " + loginResponse.getBody().getAccessToken());
    ResponseEntity<Void> userResponse =
        restTemplate.exchange(
            appUrl("/test/private"), HttpMethod.GET, new HttpEntity<Void>(httpHeaders), Void.class);

    assertEquals(HttpStatus.OK, userResponse.getStatusCode());
  }
}
