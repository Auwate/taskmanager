package com.example.taskmanager.unit.service;

import com.example.taskmanager.model.User;
import com.example.taskmanager.repository.UserRepository;
import com.example.taskmanager.service.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.test.context.ActiveProfiles;

import java.util.List;
import java.util.Optional;
import java.util.Set;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
@ActiveProfiles("test")
public class UserServiceTests {

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private UserService userService;

    private User user;

    @BeforeEach
    void setUp() {
        this.user = new User(
                1L,
                Set.of()
        );
    }

    @Test
    void testLoadByJwt() {

        UserDetails userDetails = userService.loadUserByJWT("1", List.of("USER"));

        // Assertions
        assertEquals("1", userDetails.getUsername());
        assertEquals("USER", userDetails.getAuthorities().stream().findFirst().orElseThrow().getAuthority());

    }

    @Test
    void testGetUserFromCredentials() {

        UsernamePasswordAuthenticationToken token = new UsernamePasswordAuthenticationToken(
                new org.springframework.security.core.userdetails.User(
                        "1",
                        "JWT-Authenticated",
                        List.of()
                ),
                null,
                List.of()
        );
        SecurityContextHolder.getContext().setAuthentication(token);

        when(userRepository.findById(1L)).thenReturn(Optional.of(user));

        User user = userService.getUserCredentialsFromContext();

        // Assertions
        assertEquals(1L, user.getId());
        assertTrue(user.getTasks().isEmpty());

    }

    @Test
    void testGetUserFromCredentialsEmpty() {

        UsernamePasswordAuthenticationToken token = new UsernamePasswordAuthenticationToken(
                new org.springframework.security.core.userdetails.User(
                        "1",
                        "JWT-Authenticated",
                        List.of()
                ),
                null,
                List.of()
        );
        SecurityContextHolder.getContext().setAuthentication(token);

        when(userRepository.findById(1L)).thenReturn(Optional.empty());
        when(userRepository.save(any(User.class))).thenReturn(user);

        User user = userService.getUserCredentialsFromContext();

        // Assertions
        assertEquals(1L, user.getId());
        assertTrue(user.getTasks().isEmpty());

    }

}
