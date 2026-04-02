package com.bluemarket.presentation.rest;

import com.bluemarket.application.response.UserResponse;
import com.bluemarket.application.service.UserService;

import io.smallrye.mutiny.Uni;
import jakarta.inject.Inject;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;

@Path("/users")
public class UserResources {

    @Inject
    UserService userService;

    @GET
    @Path("/{username}")
    @Produces(MediaType.APPLICATION_JSON)
    public Uni<UserResponse> getUser(@PathParam("username") String username) {
        return userService.getUserByUsername(username);
    }
}
