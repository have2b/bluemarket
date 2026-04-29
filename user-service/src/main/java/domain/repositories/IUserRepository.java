package domain.repositories;

import java.util.UUID;

import domain.entities.User;
import io.quarkus.hibernate.reactive.panache.PanacheRepositoryBase;

public interface IUserRepository extends PanacheRepositoryBase<User, UUID> {

}
