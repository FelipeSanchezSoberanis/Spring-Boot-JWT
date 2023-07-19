package com.felipe.jwtTest.filters;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.exceptions.TokenExpiredException;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.felipe.jwtTest.config.JwtConstants;
import com.felipe.jwtTest.dtos.responses.ApiErrorResponse;
import com.felipe.jwtTest.entities.User;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

public class JwtAuthorizationFilter extends OncePerRequestFilter {
  private ObjectMapper objectMapper;

  public JwtAuthorizationFilter(ObjectMapper objectMapper) {
    this.objectMapper = objectMapper;
  }

  @Override
  protected void doFilterInternal(
      HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
      throws ServletException, IOException {
    String authorizationHeader = request.getHeader(HttpHeaders.AUTHORIZATION);

    if (authorizationHeader == null) {
      filterChain.doFilter(request, response);
      return;
    }

    String accessToken = authorizationHeader.substring("Bearer".length() + 1);

    JWTVerifier jwtVerifier =
        JWT.require(JwtConstants.ALGORITHM).withIssuer(JwtConstants.ISSUER).build();

    DecodedJWT decodedJWT = null;
    try {
      decodedJWT = jwtVerifier.verify(accessToken);
    } catch (TokenExpiredException ex) {
      sendResponse("Expired access token", HttpStatus.UNAUTHORIZED, response);
      return;
    } catch (JWTVerificationException ex) {
      sendResponse("Broken jwt", HttpStatus.BAD_REQUEST, response);
      return;
    }

    if (decodedJWT
        .getClaim(JwtConstants.CLAIM_TOKEN_TYPE)
        .asString()
        .equals(JwtConstants.TOKEN_TYPE_REFRESH)) {
      sendResponse("Use the access token", HttpStatus.BAD_REQUEST, response);
      return;
    }

    Long userId = decodedJWT.getClaim(JwtConstants.CLAIM_USER_ID).asLong();
    List<String> authoritiesAsStrings =
        decodedJWT.getClaim(JwtConstants.CLAIM_AUTHORITIES).asList(String.class);

    List<SimpleGrantedAuthority> authorities =
        authoritiesAsStrings.stream().map(SimpleGrantedAuthority::new).collect(Collectors.toList());

    User user = new User();
    user.setId(userId);
    user.setAuthorities(authorities);

    Authentication authentication =
        new UsernamePasswordAuthenticationToken(user, null, authorities);
    SecurityContextHolder.getContext().setAuthentication(authentication);

    filterChain.doFilter(request, response);
  }

  private void sendResponse(String message, HttpStatus status, HttpServletResponse response)
      throws IOException {
    ApiErrorResponse apiErrorResponse = new ApiErrorResponse();
    apiErrorResponse.setMessage(message);

    response.setStatus(status.value());
    response.getWriter().write(objectMapper.writeValueAsString(apiErrorResponse));
    response.setContentType(MediaType.APPLICATION_JSON_VALUE);
  }
}
