package ru.tikskit.service.cache;

import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

@Component
public class CacheConcurrentHastMap<T> implements Cache<T> {
    private final Map<Long, T> data = new ConcurrentHashMap<>();

    @Override
    public Optional<T> get(long id) {
        return Optional.ofNullable(data.get(id));
    }

    @Override
    public Optional<T> put(long id, T t) {
        return Optional.ofNullable(data.put(id, t));
    }

    @Override
    public List<T> getAll() {
        return new ArrayList<>(data.values());
    }

    @Override
    public Optional<T> delete(long id) {
        return Optional.ofNullable(data.remove(id));
    }
}
