package ru.tikskit.service.cache;

import java.util.List;

public interface Cache<T> {
    T get(long id);

    T put(long id, T t);

    List<T> getAll();
}
