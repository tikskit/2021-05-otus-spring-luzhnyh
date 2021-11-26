package ru.tikskit.service.cache;

import java.util.List;
import java.util.Optional;

public interface Cache<T> {
    Optional<T> get(long id);

    Optional<T> put(long id, T t);

    Optional<T> delete(long id);

    List<T> getAll();
}
