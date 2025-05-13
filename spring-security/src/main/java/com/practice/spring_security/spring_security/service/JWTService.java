package com.practice.spring_security.spring_security.service;


import com.practice.spring_security.spring_security.entity.User;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Component
public class JWTService {

    @Value("${jwt.secret}")
    private String secretKey;

    public String generateToken(User user){

        Map<String,Object> claim = new HashMap<>();
        return Jwts.builder()
                .claims()
                .add(claim)
                .subject(user.getUsername())
                .issuedAt(new Date(System.currentTimeMillis()))
                .expiration(new Date(System.currentTimeMillis() + 1000 * 60 * 10))
                .and()
                .signWith(getKey())
                .compact();
    }

    public SecretKey getKey(){

        byte[] keyBytes = Decoders.BASE64URL.decode(secretKey);
        return Keys.hmacShaKeyFor(keyBytes);
    }

//    Extract the username from the JWT Bearer Token
    public String extractUsername(String token) {
        return Jwts.parser()
                .verifyWith(getKey())
                .build()
                .parseSignedClaims(token)
                .getPayload()
                .getSubject();
    }

//    Check if the Token is Valid
    public boolean validateToken(String token, UserDetails userDetails) {
        String username = extractUsername(token);
        return (username.equals(userDetails.getUsername()) && !isTokenExpired(token));
    }

//    Check the expiration date of the token
    private boolean isTokenExpired(String token) {
        Date expirationDate = Jwts.parser()
                .verifyWith(getKey())
                .build()
                .parseSignedClaims(token)
                .getPayload()
                .getExpiration();

        return expirationDate.before(new Date());
    }
}
