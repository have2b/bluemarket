package application.usecases;

import java.util.List;

import io.smallrye.mutiny.Uni;

public interface IBaseUseCase<T, ID> {
    Uni<T> getById(ID id);

    Uni<List<T>> getAll(int index, int size);
}
