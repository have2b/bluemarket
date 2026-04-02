package com.bluemarket.domain.repository;

import com.bluemarket.domain.model.User;

import io.smallrye.mutiny.Uni;

public interface IUserRepository {

    Uni<User> findByUsername(String username);
}
