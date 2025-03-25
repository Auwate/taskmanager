package com.example.taskmanager.unit.service;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.example.taskmanager.exception.server.InvalidJwtException;
import com.example.taskmanager.service.JwtService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.test.context.ActiveProfiles;

import java.time.Duration;
import java.util.Date;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@ActiveProfiles("test")
public class JwtServiceTests {

    @BeforeEach
    void setUp() {
        this.jwtService = new JwtService("Test");
    }

    private JwtService jwtService;

    private String generateToken() {
        return JWT.create()
                .withSubject("Test user")
                .withClaim("authorities", List.of("USER"))
                .withIssuedAt(new Date())
                .withExpiresAt(new Date(System.currentTimeMillis() + Duration.ofMinutes(10).toMillis()))
                .sign(Algorithm.HMAC512("Test"));
    }

    /**
     * Test that JwtService correctly validates that a JWT is not expired
     */
    @Test
    void testValidate() {

        String testJWT = generateToken();

        // Assertions
        assertTrue(jwtService.validateToken(testJWT));

    }

    /**
     * Test that JwtService correctly throws an exception with an expired JWT
     */
    @Test
    void testValidateExpired() {

        String testJwt = JWT.create()
                .withExpiresAt(new Date())
                .withIssuedAt(new Date())
                .sign(Algorithm.HMAC256("Test"));

        assertThrows(InvalidJwtException.class, () -> jwtService.validateToken(testJwt));

    }

    /**
     * Test that JwtService will throw an exception with an invalid JWT
     */
    @Test
    void testValidateInvalid() {

        String testJwt = JWT.create()
                .withExpiresAt(new Date(System.currentTimeMillis() + Duration.ofMinutes(10).toMillis()))
                .withIssuedAt(new Date())
                .sign(Algorithm.HMAC256("Invalid_secret"));

        assertThrows(InvalidJwtException.class, () -> jwtService.validateToken(testJwt));

    }

    /**
     * Test that JwtService can successfully extract the username
     */
    @Test
    void testExtractUsername() {

        String testJWT = generateToken();

        // Assertions
        assertEquals("Test user", jwtService.extractUsername(testJWT));

    }

    /**
     * Test that JwtService can successfully extract the authority
     */
    @Test
    void testExtractAuthority() {

        String testJWT = generateToken();

        // Assertions
        assertEquals("USER", jwtService.extractAuthorities(testJWT).getFirst());

    }

}
