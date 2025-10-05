package com.example.demo.security;

import com.example.demo.entities.User;
import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import io.jsonwebtoken.security.Keys;
import org.springframework.stereotype.Component;
import java.util.Base64;

import java.security.Key;
import java.util.Date;

@Component
public class JwtUtils {

    private final String jwtSecret = "my-super-secret-key-my-super-secret-key";
    private final long jwtExpirationMs = 86400000;

    private final Key key = Keys.hmacShaKeyFor(jwtSecret.getBytes());

    public Key getKey(){
        return key;
    }
    public String generateToken(User user){
        return Jwts.builder()
                .setSubject(user.getEmail())
                .claim("userId", user.getId())
                .claim("roles", user.getRoles())
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + 1000 * 60 * 60))
                .signWith(key, SignatureAlgorithm.HS256)
                .compact();
    }
    public String getEmailFromJwt(String token){
        return Jwts.parserBuilder()
                .setSigningKey(key)
                .build()
                .parseClaimsJws(token)
                .getBody()
                .getSubject();
    }
    public boolean validateToken(String token){
        try{
         Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(token);
         return true;
        } catch (JwtException e) {
            return false;
        }
    }
}


