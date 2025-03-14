package com.example.taskmanager.config;


import com.example.taskmanager.exception.handler.FilterExceptionManager;
import com.example.taskmanager.exception.server.InvalidJwtException;
import com.example.taskmanager.exception.server.JwtNotProvidedException;
import com.example.taskmanager.service.UserService;
import com.example.taskmanager.util.JwtUtil;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.List;

@Component
public class JwtRequestFilter extends OncePerRequestFilter {

    @Autowired
    public JwtRequestFilter(
            UserService userService,
            JwtUtil jwtUtil
    ) {
        this.userService = userService;
        this.jwtUtil = jwtUtil;
        this.exceptionManager = new FilterExceptionManager();
    }

    private static final Logger logger = LoggerFactory.getLogger(JwtRequestFilter.class);

    public JwtRequestFilter(
            UserService userService,
            JwtUtil jwtUtil,
            FilterExceptionManager filterExceptionManager
    ) {
        this.userService = userService;
        this.jwtUtil = jwtUtil;
        this.exceptionManager = filterExceptionManager;
    }

    private final UserService userService;
    private final JwtUtil jwtUtil;

    private final FilterExceptionManager exceptionManager;

    @Override
    protected void doFilterInternal(
            HttpServletRequest request,
            HttpServletResponse response,
            FilterChain filterChain
    ) throws ServletException, IOException {

        final String authorizationHeader = request.getHeader("Authorization");

        if (logger.isDebugEnabled()) {
            logger.debug("Authorization header: {}", authorizationHeader);
        }

        List<String> authorities = null;
        String username = null;
        String jwt;

        if (authorizationHeader == null || !authorizationHeader.startsWith("Bearer ")) {

            exceptionManager.handleJwtNotProvidedException(
                    new JwtNotProvidedException("Access token not provided."),
                    response
            );

            return;

        }

        try {

            jwt = authorizationHeader.substring(7);

            if (jwtUtil.validateToken(jwt)) {

                authorities = jwtUtil.extractAuthorities(jwt);
                username = jwtUtil.extractUsername(jwt);

                if (logger.isDebugEnabled()) {
                    logger.debug("Username: {}, Authorities: {}", username, authorities);
                }

            }

        } catch (IndexOutOfBoundsException exception) {

            exceptionManager.handleJwtNotProvidedException(
                    new JwtNotProvidedException("Access token not provided."),
                    response
            );

            return;

        } catch (InvalidJwtException exception) {

            exceptionManager.handleInvalidJwtException(
                    new InvalidJwtException("Access token not provided."),
                    response
            );

            return;

        }

        if (username != null && authorities != null && SecurityContextHolder.getContext().getAuthentication() == null) {

            UserDetails userDetails = userService.loadUserByJWT(username, authorities);

            // Create a token with the User details and their authorities
            UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken = new UsernamePasswordAuthenticationToken(
                    userDetails, null, userDetails.getAuthorities()
            );

            // Get metadata (remote IP, session details, etc.)
            usernamePasswordAuthenticationToken
                    .setDetails(new WebAuthenticationDetailsSource().buildDetails(request));

            // Set the SecurityContextHolder's authentication with the token
            SecurityContextHolder.getContext().setAuthentication(usernamePasswordAuthenticationToken);

            if (logger.isDebugEnabled()) {
                logger.debug("Authentication: {}", (
                        (UserDetails) (
                                SecurityContextHolder
                                        .getContext()
                                        .getAuthentication()
                                        .getPrincipal()
                        )).getUsername()
                );
            }

        }

        filterChain.doFilter(request, response);

    }

}
