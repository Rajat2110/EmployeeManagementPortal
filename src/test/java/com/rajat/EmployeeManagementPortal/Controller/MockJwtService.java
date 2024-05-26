package com.rajat.EmployeeManagementPortal.Controller;

import com.rajat.EmployeeManagementPortal.model.USER_ROLE;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;

import java.util.Date;

public class MockJwtService {
  private static final String SECRET_KEY = "1deab39df3d516ddd433a98907750a9d2cc4a1d7a558a75c7343aa3a95d1b5a5";
  private static final long EXPIRATION_TIME = 1000*60*5;

  public static String generateToken(String username, USER_ROLE role) {
    return Jwts.builder()
      .setSubject(username)
      .claim("role", role)
      .setExpiration(new Date(System.currentTimeMillis() + EXPIRATION_TIME))
      .signWith(SignatureAlgorithm.HS256, SECRET_KEY)
      .compact();
  }
}
