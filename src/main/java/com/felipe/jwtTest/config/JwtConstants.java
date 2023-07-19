package com.felipe.jwtTest.config;

import com.auth0.jwt.algorithms.Algorithm;
import java.util.concurrent.TimeUnit;

public class JwtConstants {
  private static final String SECRET = "some_crazy_secret";
  public static final String ISSUER = "app";
  public static final Long ACCESS_TOKEN_EXPIRATION_TIME = TimeUnit.MINUTES.toMillis(15);
  public static final Long REFRESH_TOKEN_EXPIRATION_TIME = TimeUnit.DAYS.toMillis(30);
  public static final Algorithm ALGORITHM = Algorithm.HMAC512(SECRET);
  public static final String CLAIM_TOKEN_TYPE = "tokenType";
  public static final String CLAIM_USERNAME = "username";
  public static final String CLAIM_USER_ID = "userId";
  public static final String CLAIM_AUTHORITIES = "authorities";
  public static final String TOKEN_TYPE_ACCESS = "access";
  public static final String TOKEN_TYPE_REFRESH = "refresh";
}
