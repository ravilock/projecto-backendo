package com.example.mapaCife.service;

import java.time.Clock;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneOffset;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTCreationException;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.example.mapaCife.models.User;

@Service
public class TokenService {
  @Value("${api.security.token.secret}")
  private String secret;

  public String generateToken(User user) {
    try {
      Algorithm algorithm = Algorithm.HMAC256(secret);

      return JWT.create().withIssuer("mapacife.com.br")
          .withSubject(user.getUsername())
          .withExpiresAt(getExpirationTime())
          .sign(algorithm);
    } catch (JWTCreationException e) {
      throw new RuntimeException(e);
    }
  }

  public String validateToken(String token) {
    try {
      Algorithm algorithm = Algorithm.HMAC256(secret);

      return JWT.require(algorithm)
          .withIssuer("mapacife.com.br")
          .build()
          .verify(token)
          .getSubject();
    } catch (JWTVerificationException e) {
      throw new RuntimeException(e);
    }
  }

  public Instant getExpirationTime() {
    int secondsInHour = 60 * 60;
    return Instant.now(Clock.systemUTC()).plusSeconds(secondsInHour);
  }
}
