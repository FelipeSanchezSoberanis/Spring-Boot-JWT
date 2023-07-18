package com.felipe.jwtTest.services;

import com.auth0.jwt.JWT;
import com.felipe.jwtTest.config.JwtConstants;
import com.felipe.jwtTest.dtos.requests.LoginRequest;
import com.felipe.jwtTest.entities.User;
import com.felipe.jwtTest.exceptions.ImprovedBadCredentialsException;
import java.time.Instant;
import java.util.HashSet;
import java.util.Set;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class AuthenticationService {
  @Autowired private AuthenticationManager authenticationManager;

  public Authentication authenticate(LoginRequest loginRequest) {
    Authentication authentication = null;

    try {
      authentication =
          authenticationManager.authenticate(
              new UsernamePasswordAuthenticationToken(
                  loginRequest.getUsername(), loginRequest.getPassword()));

    } catch (BadCredentialsException | UsernameNotFoundException ex) {
      throw new ImprovedBadCredentialsException();
    }

    return authentication;
  }

  public String createAccessToken(User user) {
    Set<String> userAuthorities = new HashSet<>();
    user.getRoles().forEach(r -> r.getPermissions().forEach(p -> userAuthorities.add(p.getName())));

    String[] userAuthoritiesArray = new String[userAuthorities.size()];
    userAuthorities.toArray(userAuthoritiesArray);

    return JWT.create()
        .withIssuer(JwtConstants.ISSUER)
        .withClaim("tokenType", "access")
        .withClaim("username", user.getUsername())
        .withClaim("userId", user.getId())
        .withArrayClaim("authorities", userAuthoritiesArray)
        .withExpiresAt(Instant.now().plusMillis(JwtConstants.ACCESS_TOKEN_EXPIRATION_TIME))
        .sign(JwtConstants.ALGORITHM);
  }

  public String createRefreshToken(User user) {
    return JWT.create()
        .withIssuer(JwtConstants.ISSUER)
        .withClaim("tokenType", "refresh")
        .withClaim("username", user.getUsername())
        .withClaim("userId", user.getId())
        .withExpiresAt(Instant.now().plusMillis(JwtConstants.REFRESH_TOKEN_EXPIRATION_TIME))
        .sign(JwtConstants.ALGORITHM);
  }
}
