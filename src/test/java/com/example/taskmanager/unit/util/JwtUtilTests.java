package com.example.taskmanager.unit.util;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.example.taskmanager.exception.server.InvalidJwtException;
import com.example.taskmanager.util.JwtUtil;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.Duration;
import java.util.Date;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class JwtUtilTests {

    @BeforeEach
    void setUp() {
        this.jwtUtil = new JwtUtil("Test");
    }

    private JwtUtil jwtUtil;

    private String generateToken() {
        return JWT.create()
                .withSubject("Test user")
                .withClaim("authorities", List.of("USER"))
                .withIssuedAt(new Date())
                .withExpiresAt(new Date(System.currentTimeMillis() + Duration.ofMinutes(10).toMillis()))
                .sign(Algorithm.HMAC512("Test"));
    }

    /**
     * Test that JwtUtil correctly validates that a JWT is not expired
     */
    @Test
    void testValidate() {

        String testJWT = generateToken();

        // Assertions
        assertTrue(jwtUtil.validateToken(testJWT));

    }

    /**
     * Test that JwtUtil correctly throws an exception with an expired JWT
     */
    @Test
    void testValidateExpired() {

        String testJwt = JWT.create()
                .withExpiresAt(new Date())
                .withIssuedAt(new Date())
                .sign(Algorithm.HMAC256("Test"));

        assertThrows(InvalidJwtException.class, () -> jwtUtil.validateToken(testJwt));

    }

    /**
     * Test that JwtUtil will throw an exception with an invalid JWT
     */
    @Test
    void testValidateInvalid() {

        String testJwt = JWT.create()
                .withExpiresAt(new Date(System.currentTimeMillis() + Duration.ofMinutes(10).toMillis()))
                .withIssuedAt(new Date())
                .sign(Algorithm.HMAC256("Invalid_secret"));

        assertThrows(InvalidJwtException.class, () -> jwtUtil.validateToken(testJwt));

    }

    /**
     * Test that JwtUtil can successfully extract the username
     */
    @Test
    void testExtractUsername() {

        String testJWT = generateToken();

        // Assertions
        assertEquals("Test user", jwtUtil.extractUsername(testJWT));

    }

    /**
     * Test that JwtUtil can successfully extract the authority
     */
    @Test
    void testExtractAuthority() {

        String testJWT = generateToken();

        // Assertions
        assertEquals("USER", jwtUtil.extractAuthorities(testJWT).getFirst());

    }

}
