package com.felipe.jwtTest.config;

import com.auth0.jwt.algorithms.Algorithm;
import java.util.concurrent.TimeUnit;

public class JwtConstants {
  private static final String SECRET = "some_crazy_secret";
  public static final Long ACCESS_TOKEN_EXPIRATION_TIME = TimeUnit.MINUTES.toMillis(15);
  public static final Long REFRESH_TOKEN_EXPIRATION_TIME = TimeUnit.DAYS.toMillis(30);
  public static final Algorithm ALGORITHM = Algorithm.HMAC512(SECRET);
}
