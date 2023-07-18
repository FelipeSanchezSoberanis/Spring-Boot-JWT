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
      ApiErrorResponse apiErrorResponse = new ApiErrorResponse();
      apiErrorResponse.setMessage("Expired access token");

      response.setStatus(HttpStatus.UNAUTHORIZED.value());
      response.getWriter().write(objectMapper.writeValueAsString(apiErrorResponse));
      response.setContentType(MediaType.APPLICATION_JSON_VALUE);

      filterChain.doFilter(request, response);
      return;
    } catch (JWTVerificationException ex) {
      filterChain.doFilter(request, response);
      return;
    }

    Long userId = decodedJWT.getClaim("userId").asLong();
    List<String> authoritiesAsStrings = decodedJWT.getClaim("authorities").asList(String.class);

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
}
