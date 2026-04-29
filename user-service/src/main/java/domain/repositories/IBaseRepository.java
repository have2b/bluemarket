package domain.repositories;

import java.util.List;

import io.quarkus.hibernate.reactive.panache.PanacheRepositoryBase;
import io.smallrye.mutiny.Uni;

public interface IBaseRepository<T, ID> extends PanacheRepositoryBase<T, ID> {
    Uni<T> findById(ID id);

    Uni<List<T>> findAll(int index, int size);

    Uni<ID> save();

    Uni<Void> delete();
}
