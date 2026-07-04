package com.example.bankcards.security;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.springframework.stereotype.Service;
import java.security.Key;
import java.util.Date;
import java.util.List;

@Service
public class JwtService
{
    private final String secret = "secret-key-temp-key-need-be-more-sumbols-lol";

    private final long expirationMs = 1000 * 60 * 60;

    private final Key key = Keys.hmacShaKeyFor(secret.getBytes());

    public String generateToken(String username, List<String> roles)
    {
        return Jwts.builder()
                .setSubject(username)
                .claim("roles", roles)
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + expirationMs))
                .signWith(key)
                .compact();
    }

    public Claims extractClaims(String token)
    {
        return Jwts.parser()
                .setSigningKey(key)
                .build()
                .parseClaimsJws(token)
                .getBody();

    }
}