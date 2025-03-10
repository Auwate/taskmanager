package com.example.taskmanager.service;


import com.example.taskmanager.model.Role;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.List;

@Service
public class UserService {

    private final PasswordEncodingService passwordEncoder;

    @Autowired
    public UserService(PasswordEncodingService passwordEncoder) {
        this.passwordEncoder = passwordEncoder;
    }

    private static final Logger logger = LoggerFactory.getLogger(UserService.class);

    public UserDetails loadUserByJWT(String username, List<String> authorities) {

        if (logger.isDebugEnabled()) {
            logger.debug("Attempting loadUserByJWT with {} and {}", username, authorities);
        }

        return new org.springframework.security.core.userdetails.User (
                username,
                "JWT-AUTHENTICATED",
                mapRolesToAuthorities(authorities.stream().map(Role::of).toList())
        );

    }

    private Collection<? extends GrantedAuthority> mapRolesToAuthorities(Collection<Role> roles) {
        return roles.stream()
                .map(role -> new SimpleGrantedAuthority(role.getName()))
                .toList();
    }

}
