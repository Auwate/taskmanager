package com.example.taskmanager.util;


import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTDecodeException;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.exceptions.TokenExpiredException;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.example.taskmanager.exception.server.InvalidJwtException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class JwtUtil {

    @Value("${jwt.secret}")
    private String secret;

    public String extractUsername(String token) {
        try {
            DecodedJWT jwt = JWT.decode(token);
            return jwt.getSubject();
        } catch (JWTDecodeException exception) {
            throw new InvalidJwtException("Invalid subject provided.");
        }
    }

    public List<String> extractAuthorities(String token) {
        try {
            DecodedJWT jwt = JWT.decode(token);
            return jwt.getClaim("authorities").asList(String.class);
        } catch (JWTDecodeException exception) {
            throw new InvalidJwtException("Invalid claims provided.");
        }
    }

    public boolean validateToken(String token) {
        try {
            Algorithm algorithm = Algorithm.HMAC512(secret);

            JWTVerifier verifier = JWT.require(algorithm)
                    .build();

            verifier.verify(token);

            return true;
        } catch (TokenExpiredException exception) {
            throw new InvalidJwtException("Your access token is expired.");
        } catch (JWTVerificationException exception) {
            throw new InvalidJwtException("Your access token is invalid.");
        }
    }

}
