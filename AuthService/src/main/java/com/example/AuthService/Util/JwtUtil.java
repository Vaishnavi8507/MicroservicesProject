package com.example.AuthService.Util;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.security.Key;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Component
public class JwtUtil {


    // For HMAC-SHA algorithms a key of at least 32 bytes is recommended
    public static final String SECRET_KEY = "01234567890123456789012345678901";

    public static String generateToken(String username){
        Map<String, Object> claims = new HashMap<>();
        claims.put("email", "vaish@gmail.com");

        return Jwts.builder()
                .subject(username)
                .issuedAt(new Date(System.currentTimeMillis()))
                .expiration(new Date(System.currentTimeMillis() + 1000 * 60 * 5))
                .claims(claims)
                .signWith(getKey())
                .compact();
    }

    private static Key getKey() {
        return Keys.hmacShaKeyFor(SECRET_KEY.getBytes(StandardCharsets.UTF_8));
    }

    private static Claims getClaims(String token){
        return Jwts.parser().verifyWith((SecretKey) getKey())
                .build().parseSignedClaims(token)
                .getPayload();
    }

    public static Date extractExpiration(String token) {
        return getClaims(token).getExpiration();
    }
}
