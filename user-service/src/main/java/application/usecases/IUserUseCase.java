package application.usecases;

import java.util.UUID;

import application.dto.UserInfoRes;
import io.smallrye.mutiny.Uni;

public interface IUserUseCase extends IBaseUseCase<UserInfoRes, UUID> {
    Uni<UserInfoRes> getUserByEmail(String email);

    Uni<UserInfoRes> getUserByUsername(String username);

    Uni<UUID> createUser(String firstName, String lastName, String username, String email, String password);

    Uni<Void> updateUser(UUID id, String firstName, String lastName, String email);

    Uni<Void> deleteUser(UUID id);
}
