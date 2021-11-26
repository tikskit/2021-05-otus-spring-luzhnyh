package ru.tikskit.service.cache;

import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Component
public class CacheConcurrentHastMap<T> implements Cache<T> {
    private final Map<Long, T> data = new ConcurrentHashMap<>();

    @Override
    public T get(long id) {
        return data.get(id);
    }

    @Override
    public T put(long id, T t) {
        return data.put(id, t);
    }

    @Override
    public List<T> getAll() {
        return new ArrayList<>(data.values());
    }
}
