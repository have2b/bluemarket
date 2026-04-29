package application.mappers;

import application.dto.UserInfoRes;
import domain.entities.User;
import jakarta.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class UserMapper {
    public UserInfoRes toDto(User user) {
        return new UserInfoRes(
                user.getId(),
                user.getFirstName(),
                user.getLastName(),
                user.getUsername(),
                user.getEmail(),
                user.getRole(),
                user.getStatus());
    }
}
