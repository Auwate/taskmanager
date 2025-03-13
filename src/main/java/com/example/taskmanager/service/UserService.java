package com.example.taskmanager.service;


import com.example.taskmanager.model.Role;
import com.example.taskmanager.model.Task;
import com.example.taskmanager.model.User;
import com.example.taskmanager.repository.UserRepository;
import jakarta.transaction.Transactional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.event.Level;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.Set;

@Service
public class UserService {

    private final UserRepository userRepository;

    private static final Logger logger = LoggerFactory.getLogger(UserService.class);

    @Autowired
    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    private Collection<? extends GrantedAuthority> mapRolesToAuthorities(Collection<Role> roles) {
        return roles.stream()
                .map(role -> new SimpleGrantedAuthority(role.getName()))
                .toList();
    }

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

    public User getUserCredentialsFromContext() {

        if (logger.isEnabledForLevel(Level.DEBUG)) {
            logger.debug("Getting user authorization credentials...");
        }

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        UserDetails userDetails = (UserDetails)authentication.getPrincipal();
        Long userId = Long.decode(userDetails.getUsername());

        Optional<User> dbUser = userRepository.findById(userId);
        User user;

        if (dbUser.isEmpty()) {
            if (logger.isEnabledForLevel(Level.DEBUG)) {
                logger.debug("Empty user found.");
            }
            user = saveUser(new User(userId, Set.of()));
        } else {
            user = dbUser.get();
        }

        if (logger.isDebugEnabled()) {
            logger.debug("Credentials found: {}, {}", user.getId(), user.getTasks());
        }

        return user;

    }

    public void removeAndSave(User user, Task task) {

        if (logger.isEnabledForLevel(Level.DEBUG)) {
            logger.debug("Attempting to remove task from user.tasks");
        }

        user.removeTask(task);
        saveUser(user);

    }

    @Transactional
    public User saveUser(User user) {

        if (logger.isEnabledForLevel(Level.DEBUG)) {
            logger.debug("Attempting to save user to database...");
        }

        return userRepository.save(user);

    }

}
