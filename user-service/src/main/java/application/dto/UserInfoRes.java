package application.dto;

import java.util.UUID;

import domain.valueobjects.UserRole;
import domain.valueobjects.UserStatus;

public record UserInfoRes(
        UUID id,
        String firstName,
        String lastName,
        String username,
        String email,
        UserRole role,
        UserStatus status) {

}
