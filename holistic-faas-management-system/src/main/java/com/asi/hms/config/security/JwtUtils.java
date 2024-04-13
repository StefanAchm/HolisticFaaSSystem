package com.asi.hms.config.security;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.security.Key;
import java.util.Date;

@Component
public class JwtUtils {

    // TODO: change this to a more secure value
    // For now, just use a secret, with >= 256 bits of entropy for the HMAC-SHA256 algorithm
    private static final String SECRET = "my-secret-key-1234567891011121314";

    private static final long EXPIRATION_TIME = 864_000_000; // 10 days

    public String getUserNameFromJwtToken(String jwt) {

        return Jwts.parser()
                .verifyWith((SecretKey) getSigningKey())
                .build()
                .parseSignedClaims(jwt)
                .getPayload()
                .getSubject();

    }

    public boolean validateJwtToken(String jwt) {

        try {

            Jwts.parser()
                    .verifyWith((SecretKey) getSigningKey())
                    .build()
                    .parseSignedClaims(jwt);

            return true;

        } catch (Exception e) {
            return false;
        }

    }

    public String generateToken(String username) {

        return Jwts.builder()
                .subject(username)
                .expiration(new Date(System.currentTimeMillis() + EXPIRATION_TIME))
                .signWith(getSigningKey())
                .compact();

    }

    private Key getSigningKey() {

        byte[] keyBytes = SECRET.getBytes(StandardCharsets.UTF_8);

        return Keys.hmacShaKeyFor(keyBytes);
    }

}
