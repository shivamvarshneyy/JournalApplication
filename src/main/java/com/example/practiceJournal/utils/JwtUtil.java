package com.example.practiceJournal.utils;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.SplittableRandom;

@Component
public class JwtUtil {
    @Value("${jwt.secret.key}")
    private String SECRET_KEY;

    private SecretKey getSigningKey(){
        return Keys.hmacShaKeyFor(SECRET_KEY.getBytes());
    }

    public String extractUsername(String jwtToken){
        return extractClaims(jwtToken).getSubject();
    }

    public Date extractExpiration(String jwtToken){
        return extractClaims(jwtToken).getExpiration();
    }

    public boolean isTokenExpire(String jwtToken){
        return extractExpiration(jwtToken).before(new Date());
    }

    public Claims extractClaims(String jwtToken){
        return Jwts.parser()
                .verifyWith(getSigningKey())
                .build()
                .parseSignedClaims(jwtToken)
                .getPayload();
    }

    public String generateJwtToken(String username){
        Map<String, Object> claims = new HashMap<>();
        return createJwtToken(claims, username);
    }

    public String createJwtToken(Map<String, Object> claims, String subject){
        return Jwts.builder()
                .subject(subject)
                .claims(claims)
                .header().empty().add("typ","JWT")
                .and()
                .issuedAt(new Date(System.currentTimeMillis()))
                .expiration(new Date(System.currentTimeMillis() + 1000*60*10))
                .signWith(getSigningKey())
                .compact();
    }

    public boolean validateToken(String jwtToken){
        return !isTokenExpire(jwtToken);
    }
}
