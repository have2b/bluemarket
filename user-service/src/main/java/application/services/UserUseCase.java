package application.services;

import java.util.List;
import java.util.UUID;

import application.dto.UserInfoRes;
import application.mappers.UserMapper;
import domain.entities.User;
import domain.repositories.IUserRepository;
import domain.valueobjects.UserRole;
import domain.valueobjects.UserStatus;
import io.smallrye.mutiny.Uni;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;

@ApplicationScoped
public class UserUseCase implements application.usecases.IUserUseCase {
    @Inject
    IUserRepository userRepository;

    @Inject
    UserMapper userMapper;

    @Override
    public Uni<UserInfoRes> getById(UUID id) {
        return userRepository.findById(id)
                .onItem().transform(userMapper::toDto);
    }

    @Override
    public Uni<List<UserInfoRes>> getAll(int index, int size) {
        return userRepository.findAll()
                .range(index, index + size)
                .list()
                .onItem().transform(users -> users.stream()
                        .map(userMapper::toDto)
                        .toList());
    }

    @Override
    public Uni<UserInfoRes> getUserByEmail(String email) {
        return userRepository.find("email", email)
                .firstResult()
                .onItem().transform(user -> user != null ? userMapper.toDto((User) user) : null);
    }

    @Override
    public Uni<UserInfoRes> getUserByUsername(String username) {
        return userRepository.find("username", username)
                .firstResult()
                .onItem().transform(user -> user != null ? userMapper.toDto((User) user) : null);
    }

    @Override
    @Transactional
    public Uni<UUID> createUser(String firstName, String lastName, String username, String email, String password) {
        User user = new User();
        user.setFirstName(firstName);
        user.setLastName(lastName);
        user.setUsername(username);
        user.setEmail(email);
        user.setPassword(password);
        user.setRole(UserRole.CUSTOMER);
        user.setStatus(UserStatus.ACTIVE);

        return userRepository.persist(user)
                .onItem().transform(ignored -> user.getId());
    }

    @Override
    @Transactional
    public Uni<Void> updateUser(UUID id, String firstName, String lastName, String email) {
        return userRepository.findById(id)
                .onItem().ifNotNull().invoke(user -> {
                    user.setFirstName(firstName);
                    user.setLastName(lastName);
                    user.setEmail(email);
                })
                .onItem().transformToUni(user -> {
                    if (user != null) {
                        return userRepository.persist(user).replaceWithVoid();
                    }
                    return Uni.createFrom().voidItem();
                });
    }

    @Override
    @Transactional
    public Uni<Void> deleteUser(UUID id) {
        return userRepository.deleteById(id)
                .replaceWithVoid();
    }
}
