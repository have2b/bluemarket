package com.bluemarket.application.service;

import com.bluemarket.application.response.UserResponse;
import com.bluemarket.domain.exception.UserNotFoundException;
import com.bluemarket.domain.repository.IUserRepository;

import io.smallrye.mutiny.Uni;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;

@ApplicationScoped
public class UserService {

    @Inject
    IUserRepository userRepository;

    public Uni<UserResponse> getUserByUsername(String username) {
        return userRepository.findByUsername(username)
                .onItem().ifNull().failWith(() -> new UserNotFoundException("User not found: " + username))
                .map(user -> new UserResponse(
                        user.getId(),
                        user.getName(),
                        user.getUsername(),
                        user.getEmail(),
                        user.getCreatedAt()));
    }
}
